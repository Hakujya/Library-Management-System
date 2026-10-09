package com.kaizen.lms.exception;

/**
 * Thrown when attempting to issue a book that currently has zero available copies.
 */
public class BookNotAvailableException extends LibraryException {

    public BookNotAvailableException(String message) {
        super(message);
    }

    public BookNotAvailableException(String bookTitle, Long bookId) {
        super(String.format("No copies available for book '%s' (ID: %d).", bookTitle, bookId));
    }
}

