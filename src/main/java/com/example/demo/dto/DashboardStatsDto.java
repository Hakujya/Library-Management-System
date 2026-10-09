package com.example.demo.dto;

import com.example.demo.entity.Loan;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object encapsulating computed system statistics for the dashboard.
 */
public class DashboardStatsDto {

    private long totalBooks;
    private long totalCopies;
    private long availableCopies;
    private long issuedCopies;
    private long totalMembers;
    private long activeMembers;
    private long currentlyIssuedLoans;
    private long overdueLoans;
    private long returnedLoans;
    private Map<String, Long> categoryCounts;
    private List<Loan> recentActivity;

    public DashboardStatsDto() {
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(long totalCopies) {
        this.totalCopies = totalCopies;
    }

    public long getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(long availableCopies) {
        this.availableCopies = availableCopies;
    }

    public long getIssuedCopies() {
        return issuedCopies;
    }

    public void setIssuedCopies(long issuedCopies) {
        this.issuedCopies = issuedCopies;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public long getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(long activeMembers) {
        this.activeMembers = activeMembers;
    }

    public long getCurrentlyIssuedLoans() {
        return currentlyIssuedLoans;
    }

    public void setCurrentlyIssuedLoans(long currentlyIssuedLoans) {
        this.currentlyIssuedLoans = currentlyIssuedLoans;
    }

    public long getOverdueLoans() {
        return overdueLoans;
    }

    public void setOverdueLoans(long overdueLoans) {
        this.overdueLoans = overdueLoans;
    }

    public long getReturnedLoans() {
        return returnedLoans;
    }

    public void setReturnedLoans(long returnedLoans) {
        this.returnedLoans = returnedLoans;
    }

    public Map<String, Long> getCategoryCounts() {
        return categoryCounts;
    }

    public void setCategoryCounts(Map<String, Long> categoryCounts) {
        this.categoryCounts = categoryCounts;
    }

    public List<Loan> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<Loan> recentActivity) {
        this.recentActivity = recentActivity;
    }
}

