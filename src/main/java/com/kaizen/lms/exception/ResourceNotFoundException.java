package com.kaizen.lms.exception;

/**
 * Thrown when an entity (Book, Member, Loan) cannot be found by its identifier.
 */
public class ResourceNotFoundException extends LibraryException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with ID %d was not found.", resourceName, id));
    }
}

