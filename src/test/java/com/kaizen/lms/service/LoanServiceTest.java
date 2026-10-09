package com.kaizen.lms.service;

import com.kaizen.lms.dto.LoanIssueFormDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.BookCategory;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.exception.BookNotAvailableException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.repository.BookRepository;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.repository.MemberRepository;
import com.kaizen.lms.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying LoanService circulation rules, inventory tracking,
 * and transactional state safety.
 */
@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Book testBook;
    private Member testMember;

    @BeforeEach
    void setUp() {
        testBook = new Book("Clean Architecture", "Robert Martin", "978-0134494166",
                BookCategory.COMPUTER_SCIENCE, 3, 3);
        testBook.setId(10L);

        testMember = new Member("MEM-1001", "Aarav Sharma", "aarav@college.edu",
                "+91 98765 43210", LocalDate.now(), MemberStatus.ACTIVE);
        testMember.setId(20L);
    }

    @Test
    @DisplayName("Successfully issue an available book and decrement inventory")
    void testIssueBook_Success() {
        LoanIssueFormDto formDto = new LoanIssueFormDto(10L, 20L);
        formDto.setIssueDate(LocalDate.now());
        formDto.setDueDate(LocalDate.now().plusDays(14));

        when(bookRepository.findById(10L)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(testMember));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan l = invocation.getArgument(0);
            l.setId(100L);
            return l;
        });

        Loan issuedLoan = loanService.issueBook(formDto);

        assertNotNull(issuedLoan);
        assertEquals(100L, issuedLoan.getId());
        assertEquals(LoanStatus.ISSUED, issuedLoan.getStatus());
        assertEquals(2, testBook.getAvailableCopies()); // 3 -> 2
        verify(bookRepository, times(1)).save(testBook);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Prevent issuing a book when stock is zero")
    void testIssueBook_OutOfStock_ThrowsBookNotAvailableException() {
        testBook.setAvailableCopies(0); // No copies available

        LoanIssueFormDto formDto = new LoanIssueFormDto(10L, 20L);

        when(bookRepository.findById(10L)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(testMember));

        assertThrows(BookNotAvailableException.class, () -> loanService.issueBook(formDto));
        verify(loanRepository, never()).save(any(Loan.class));
        verify(bookRepository, never()).save(testBook);
    }

    @Test
    @DisplayName("Prevent issuing a book to a suspended or inactive member")
    void testIssueBook_SuspendedMember_ThrowsInvalidLoanOperationException() {
        testMember.setStatus(MemberStatus.SUSPENDED);

        LoanIssueFormDto formDto = new LoanIssueFormDto(10L, 20L);

        when(bookRepository.findById(10L)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(testMember));

        assertThrows(InvalidLoanOperationException.class, () -> loanService.issueBook(formDto));
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    @DisplayName("Prevent issuing when due date is earlier than issue date")
    void testIssueBook_InvalidDueDate_ThrowsException() {
        LoanIssueFormDto formDto = new LoanIssueFormDto(10L, 20L);
        formDto.setIssueDate(LocalDate.now());
        formDto.setDueDate(LocalDate.now().minusDays(1)); // Invalid due date

        when(bookRepository.findById(10L)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(20L)).thenReturn(Optional.of(testMember));

        assertThrows(InvalidLoanOperationException.class, () -> loanService.issueBook(formDto));
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    @DisplayName("Successfully return an issued book and restore copy inventory")
    void testReturnBook_Success() {
        testBook.setAvailableCopies(2); // Currently 2 of 3 available
        Loan activeLoan = new Loan(testBook, testMember, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        activeLoan.setId(100L);
        activeLoan.setStatus(LoanStatus.ISSUED);

        when(loanRepository.findById(100L)).thenReturn(Optional.of(activeLoan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan returned = loanService.returnBook(100L, LocalDate.now(), "Returned in good shape");

        assertNotNull(returned);
        assertEquals(LoanStatus.RETURNED, returned.getStatus());
        assertNotNull(returned.getReturnDate());
        assertEquals(3, testBook.getAvailableCopies()); // 2 -> 3 restored!
        verify(bookRepository, times(1)).save(testBook);
        verify(loanRepository, times(1)).save(activeLoan);
    }

    @Test
    @DisplayName("Prevent returning the same loan twice")
    void testReturnBook_DuplicateReturn_ThrowsException() {
        Loan returnedLoan = new Loan(testBook, testMember, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
        returnedLoan.setId(100L);
        returnedLoan.setStatus(LoanStatus.RETURNED);
        returnedLoan.setReturnDate(LocalDate.now().minusDays(7));

        when(loanRepository.findById(100L)).thenReturn(Optional.of(returnedLoan));

        assertThrows(InvalidLoanOperationException.class, () -> loanService.returnBook(100L, LocalDate.now(), null));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Identify overdue loans using Java Streams")
    void testGetOverdueLoans_IdentifiesOverdueRecords() {
        Loan l1 = new Loan(testBook, testMember, LocalDate.now().minusDays(20), LocalDate.now().minusDays(5)); // OVERDUE
        l1.setStatus(LoanStatus.ISSUED);

        Loan l2 = new Loan(testBook, testMember, LocalDate.now().minusDays(3), LocalDate.now().plusDays(11)); // NOT OVERDUE
        l2.setStatus(LoanStatus.ISSUED);

        Loan l3 = new Loan(testBook, testMember, LocalDate.now().minusDays(30), LocalDate.now().minusDays(15)); // RETURNED
        l3.setStatus(LoanStatus.RETURNED);

        when(loanRepository.findAll()).thenReturn(Arrays.asList(l1, l2, l3));

        List<Loan> overdue = loanService.getOverdueLoans();

        assertEquals(1, overdue.size());
        assertTrue(overdue.contains(l1));
    }
}

