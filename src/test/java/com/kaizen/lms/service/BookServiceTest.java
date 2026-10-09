package com.kaizen.lms.service;

import com.kaizen.lms.dto.BookFormDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.enums.BookCategory;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.exception.DuplicateResourceException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.exception.ResourceNotFoundException;
import com.kaizen.lms.repository.BookRepository;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying BookService business rules and constraints.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;
    private BookFormDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleBook = new Book("Effective Java", "Joshua Bloch", "978-0134685991",
                BookCategory.COMPUTER_SCIENCE, 5, 5);
        sampleBook.setId(1L);

        sampleDto = new BookFormDto();
        sampleDto.setTitle("Effective Java");
        sampleDto.setAuthor("Joshua Bloch");
        sampleDto.setIsbn("978-0134685991");
        sampleDto.setCategory(BookCategory.COMPUTER_SCIENCE);
        sampleDto.setTotalCopies(5);
        sampleDto.setAvailableCopies(5);
    }

    @Test
    @DisplayName("Successfully add a new book when data is valid")
    void testSaveBook_Success() {
        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(10L);
            return b;
        });

        Book saved = bookService.saveBook(sampleDto);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        assertEquals("Effective Java", saved.getTitle());
        assertEquals(5, saved.getAvailableCopies());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Reject adding a book with duplicate ISBN")
    void testSaveBook_DuplicateIsbn_ThrowsException() {
        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> bookService.saveBook(sampleDto));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Reject updating total copies below currently issued copies")
    void testUpdateBook_ReduceCopiesBelowIssued_ThrowsException() {
        sampleBook.setTotalCopies(5);
        sampleBook.setAvailableCopies(2); // 3 copies currently borrowed
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.existsByIsbnAndIdNot("978-0134685991", 1L)).thenReturn(false);

        // Try reducing totalCopies to 2 (which is less than 3 issued)
        sampleDto.setTotalCopies(2);

        assertThrows(InvalidLoanOperationException.class, () -> bookService.updateBook(1L, sampleDto));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Prevent deleting a book when active loans exist")
    void testDeleteBook_WithActiveLoans_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(loanRepository.existsByBookIdAndStatus(1L, LoanStatus.ISSUED)).thenReturn(true);

        assertThrows(InvalidLoanOperationException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    @DisplayName("Successfully delete a book when no active loans exist")
    void testDeleteBook_NoActiveLoans_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(loanRepository.existsByBookIdAndStatus(1L, LoanStatus.ISSUED)).thenReturn(false);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).delete(sampleBook);
    }

    @Test
    @DisplayName("Filter available books using Java Streams")
    void testGetAvailableBooks_FiltersCorrectly() {
        Book b1 = new Book("Book 1", "Author 1", "ISBN-1", BookCategory.FICTION, 3, 2);
        Book b2 = new Book("Book 2", "Author 2", "ISBN-2", BookCategory.SCIENCE, 2, 0); // Out of stock
        Book b3 = new Book("Book 3", "Author 3", "ISBN-3", BookCategory.HISTORY, 4, 4);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(b1, b2, b3));

        List<Book> available = bookService.getAvailableBooks();

        assertEquals(2, available.size());
        assertTrue(available.contains(b1));
        assertFalse(available.contains(b2));
        assertTrue(available.contains(b3));
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when book ID is non-existent")
    void testGetBookById_NotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(999L));
    }
}

