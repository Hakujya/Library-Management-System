package com.kaizen.lms.exception;

/**
 * Base unchecked exception for Library Management domain exceptions.
 * Demonstrates exception inheritance and hierarchy.
 */
public class LibraryException extends RuntimeException {

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}

