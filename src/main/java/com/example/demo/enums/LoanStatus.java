package com.example.demo.enums;

/**
 * Enumeration representing the current lifecycle status of a book loan.
 * Demonstrates Java Enum types with custom properties and helper methods.
 */
public enum LoanStatus {
    ISSUED("Issued", "bg-primary"),
    RETURNED("Returned", "bg-success"),
    OVERDUE("Overdue", "bg-danger");

    private final String displayName;
    private final String badgeClass;

    LoanStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}

