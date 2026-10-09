package com.kaizen.lms.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Data Transfer Object for issuing a book copy to a library member.
 */
public class LoanIssueFormDto {

    @NotNull(message = "Please select a book to issue")
    private Long bookId;

    @NotNull(message = "Please select a member")
    private Long memberId;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate = LocalDate.now();

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate = LocalDate.now().plusDays(14);

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    public LoanIssueFormDto() {
    }

    public LoanIssueFormDto(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14);
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

