package com.example.demo.enums;

/**
 * Enumeration representing book genre categories in the library catalog.
 */
public enum BookCategory {
    COMPUTER_SCIENCE("Computer Science & IT"),
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science & Mathematics"),
    ENGINEERING("Engineering"),
    HISTORY("History & Civilization"),
    PHILOSOPHY("Philosophy"),
    LITERATURE("Literature & Poetry"),
    BIOGRAPHY("Biography & Memoir"),
    BUSINESS("Business & Economics"),
    OTHER("Other");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

