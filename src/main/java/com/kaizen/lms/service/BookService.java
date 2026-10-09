package com.kaizen.lms.service;

import com.kaizen.lms.dto.BookFormDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.enums.BookCategory;

import java.util.List;

/**
 * Service interface for book catalog operations.
 * Demonstrates abstraction, interface-driven architecture, and loose coupling.
 */
public interface BookService {

    List<Book> getAllBooks();

    List<Book> searchBooks(String keyword);

    List<Book> getBooksByCategory(BookCategory category);

    List<Book> getAvailableBooks();

    Book getBookById(Long id);

    Book getBookByIsbn(String isbn);

    Book saveBook(BookFormDto formDto);

    Book updateBook(Long id, BookFormDto formDto);

    void deleteBook(Long id);

    BookFormDto toDto(Book book);
}

