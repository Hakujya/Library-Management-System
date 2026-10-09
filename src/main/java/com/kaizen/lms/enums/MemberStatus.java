package com.kaizen.lms.enums;

/**
 * Enumeration representing the status of a registered library member.
 */
public enum MemberStatus {
    ACTIVE("Active", "bg-success"),
    SUSPENDED("Suspended", "bg-warning text-dark"),
    INACTIVE("Inactive", "bg-secondary");

    private final String displayName;
    private final String badgeClass;

    MemberStatus(String displayName, String badgeClass) {
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

