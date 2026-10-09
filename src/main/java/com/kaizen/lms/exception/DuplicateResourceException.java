package com.kaizen.lms.exception;

/**
 * Thrown when attempting to register a unique constraint duplicate (e.g. ISBN or Member Email).
 */
public class DuplicateResourceException extends LibraryException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String fieldName, String value) {
        super(String.format("A record with %s '%s' already exists in the system.", fieldName, value));
    }
}

