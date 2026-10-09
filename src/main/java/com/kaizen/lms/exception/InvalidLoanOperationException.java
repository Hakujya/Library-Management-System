package com.kaizen.lms.exception;

/**
 * Thrown when an illegal loan action is performed (e.g. returning an already returned book,
 * or deleting a book/member with active borrowings).
 */
public class InvalidLoanOperationException extends LibraryException {

    public InvalidLoanOperationException(String message) {
        super(message);
    }
}

