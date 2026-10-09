package com.kaizen.lms.service.impl;

import com.kaizen.lms.dto.BookFormDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.enums.BookCategory;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.exception.DuplicateResourceException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.exception.ResourceNotFoundException;
import com.kaizen.lms.repository.BookRepository;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BookService providing business validation, inventory tracking,
 * and data integrity guarantees.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public BookServiceImpl(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.searchBooks(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByCategory(BookCategory category) {
        if (category == null) {
            return getAllBooks();
        }
        return bookRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        // Demonstrating Java Streams with filter and collect
        return bookRepository.findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ISBN " + isbn + " not found."));
    }

    @Override
    public Book saveBook(BookFormDto formDto) {
        String cleanIsbn = formDto.getIsbn().trim();
        if (bookRepository.existsByIsbn(cleanIsbn)) {
            throw new DuplicateResourceException("ISBN", cleanIsbn);
        }

        int total = formDto.getTotalCopies() != null ? formDto.getTotalCopies() : 1;
        int available = formDto.getAvailableCopies() != null ? formDto.getAvailableCopies() : total;
        if (available > total) {
            available = total;
        }

        Book book = new Book();
        populateBookFromDto(book, formDto, cleanIsbn, total, available);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, BookFormDto formDto) {
        Book book = getBookById(id);
        String cleanIsbn = formDto.getIsbn().trim();

        if (bookRepository.existsByIsbnAndIdNot(cleanIsbn, id)) {
            throw new DuplicateResourceException("ISBN", cleanIsbn);
        }

        int currentIssued = book.getIssuedCopies();
        int newTotal = formDto.getTotalCopies() != null ? formDto.getTotalCopies() : book.getTotalCopies();

        if (newTotal < currentIssued) {
            throw new InvalidLoanOperationException(String.format(
                    "Total copies (%d) cannot be less than currently issued copies (%d).",
                    newTotal, currentIssued));
        }

        // Adjust available copies based on total change while preserving issued count
        int newAvailable = newTotal - currentIssued;

        populateBookFromDto(book, formDto, cleanIsbn, newTotal, newAvailable);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        // Enforce safety rule: Prevent deletion when active loans exist
        boolean hasActiveLoans = loanRepository.existsByBookIdAndStatus(id, LoanStatus.ISSUED);
        if (hasActiveLoans) {
            throw new InvalidLoanOperationException(String.format(
                    "Cannot delete '%s' because there are active borrowed loans associated with it.",
                    book.getTitle()));
        }

        bookRepository.delete(book);
    }

    @Override
    public BookFormDto toDto(Book book) {
        BookFormDto dto = new BookFormDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setCategory(book.getCategory());
        dto.setPublisher(book.getPublisher());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setLocationShelf(book.getLocationShelf());
        dto.setDescription(book.getDescription());
        return dto;
    }

    private void populateBookFromDto(Book book, BookFormDto dto, String cleanIsbn, int total, int available) {
        book.setTitle(dto.getTitle().trim());
        book.setAuthor(dto.getAuthor().trim());
        book.setIsbn(cleanIsbn);
        book.setCategory(dto.getCategory());
        book.setPublisher(dto.getPublisher() != null ? dto.getPublisher().trim() : null);
        book.setPublicationYear(dto.getPublicationYear());
        book.setTotalCopies(total);
        book.setAvailableCopies(available);
        book.setLocationShelf(dto.getLocationShelf() != null ? dto.getLocationShelf().trim() : null);
        book.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
    }
}

