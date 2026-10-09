package com.kaizen.lms.repository;

import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA Repository for Loan entity.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByStatusOrderByIssueDateDesc(LoanStatus status);

    List<Loan> findByMemberIdOrderByIssueDateDesc(Long memberId);

    List<Loan> findByBookIdOrderByIssueDateDesc(Long bookId);

    boolean existsByBookIdAndStatus(Long bookId, LoanStatus status);

    boolean existsByMemberIdAndStatus(Long memberId, LoanStatus status);

    long countByStatus(LoanStatus status);

    long countByBookIdAndStatus(Long bookId, LoanStatus status);

    long countByMemberIdAndStatus(Long memberId, LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.status = 'ISSUED' AND l.dueDate < :today ORDER BY l.dueDate ASC")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = 'ISSUED' AND l.dueDate < :today")
    long countOverdueLoans(@Param("today") LocalDate today);

    List<Loan> findTop10ByOrderByCreatedAtDesc();

    List<Loan> findAllByOrderByCreatedAtDesc();
}

