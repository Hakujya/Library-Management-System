package com.kaizen.lms.service;

import com.kaizen.lms.dto.DashboardStatsDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.BookCategory;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.repository.BookRepository;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.repository.MemberRepository;
import com.kaizen.lms.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying real-time dashboard calculations and stream aggregations.
 */
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    @DisplayName("Compute correct dashboard statistics from database records")
    void testGetDashboardStatistics_AccurateComputation() {
        Book b1 = new Book("Book 1", "Author 1", "ISBN-1", BookCategory.COMPUTER_SCIENCE, 5, 3);
        Book b2 = new Book("Book 2", "Author 2", "ISBN-2", BookCategory.SCIENCE, 4, 4);
        List<Book> books = Arrays.asList(b1, b2);

        Member m1 = new Member("MEM-1", "User 1", "u1@test.com", "111", LocalDate.now(), MemberStatus.ACTIVE);
        Member m2 = new Member("MEM-2", "User 2", "u2@test.com", "222", LocalDate.now(), MemberStatus.INACTIVE);
        List<Member> members = Arrays.asList(m1, m2);

        Loan l1 = new Loan(b1, m1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9)); // ISSUED active
        l1.setStatus(LoanStatus.ISSUED);

        Loan l2 = new Loan(b1, m2, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6)); // ISSUED overdue
        l2.setStatus(LoanStatus.ISSUED);

        Loan l3 = new Loan(b2, m1, LocalDate.now().minusDays(15), LocalDate.now().minusDays(1)); // RETURNED
        l3.setStatus(LoanStatus.RETURNED);
        l3.setReturnDate(LocalDate.now().minusDays(2));

        List<Loan> loans = Arrays.asList(l1, l2, l3);

        when(bookRepository.count()).thenReturn(2L);
        when(bookRepository.sumTotalCopies()).thenReturn(9L);
        when(bookRepository.sumAvailableCopies()).thenReturn(7L);
        when(bookRepository.findAll()).thenReturn(books);

        when(memberRepository.count()).thenReturn(2L);
        when(memberRepository.findAll()).thenReturn(members);

        when(loanRepository.findAll()).thenReturn(loans);
        when(loanRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(Collections.singletonList(l1));

        DashboardStatsDto stats = dashboardService.getDashboardStatistics();

        assertNotNull(stats);
        assertEquals(2, stats.getTotalBooks());
        assertEquals(9, stats.getTotalCopies());
        assertEquals(7, stats.getAvailableCopies());
        assertEquals(2, stats.getIssuedCopies()); // 9 - 7 = 2
        assertEquals(2, stats.getTotalMembers());
        assertEquals(1, stats.getActiveMembers()); // 1 active out of 2
        assertEquals(2, stats.getCurrentlyIssuedLoans()); // l1 and l2
        assertEquals(1, stats.getOverdueLoans()); // l2 is overdue
        assertEquals(1, stats.getReturnedLoans()); // l3 is returned
        assertEquals(1, stats.getCategoryCounts().get(BookCategory.COMPUTER_SCIENCE.getDisplayName()));
        assertEquals(1, stats.getCategoryCounts().get(BookCategory.SCIENCE.getDisplayName()));
    }
}

