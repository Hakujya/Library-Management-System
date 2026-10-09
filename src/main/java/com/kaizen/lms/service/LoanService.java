package com.kaizen.lms.service;

import com.kaizen.lms.dto.LoanIssueFormDto;
import com.kaizen.lms.entity.Loan;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface managing book circulation, issuance, and returns.
 */
public interface LoanService {

    Loan issueBook(LoanIssueFormDto dto);

    Loan returnBook(Long loanId, LocalDate returnDate, String notes);

    List<Loan> getAllLoans();

    List<Loan> getActiveLoans();

    List<Loan> getOverdueLoans();

    List<Loan> getReturnedLoans();

    Loan getLoanById(Long id);

    List<Loan> getLoansByMember(Long memberId);

    List<Loan> getLoansByBook(Long bookId);

    List<Loan> getRecentLoans(int limit);
}

