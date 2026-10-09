package com.kaizen.lms.service.impl;

import com.kaizen.lms.dto.DashboardStatsDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.repository.BookRepository;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.repository.MemberRepository;
import com.kaizen.lms.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of DashboardService providing live calculated metrics
 * from database records using Java Streams and aggregations.
 */
@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public DashboardServiceImpl(BookRepository bookRepository,
                                MemberRepository memberRepository,
                                LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public DashboardStatsDto getDashboardStatistics() {
        DashboardStatsDto stats = new DashboardStatsDto();

        long totalBooks = bookRepository.count();
        long totalCopies = bookRepository.sumTotalCopies();
        long availableCopies = bookRepository.sumAvailableCopies();
        long issuedCopies = Math.max(0, totalCopies - availableCopies);

        long totalMembers = memberRepository.count();

        // Demonstrating Java Streams filter on members
        List<Member> allMembers = memberRepository.findAll();
        long activeMembers = allMembers.stream()
                .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
                .count();

        LocalDate today = LocalDate.now();
        List<Loan> allLoans = loanRepository.findAll();

        // Demonstrating Java Streams grouping and counting on books
        List<Book> allBooks = bookRepository.findAll();
        Map<String, Long> categoryCounts = allBooks.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getCategory().getDisplayName(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        // Demonstrating Java Streams filtering for loan status calculations
        long currentlyIssued = allLoans.stream()
                .filter(l -> l.getStatus() == LoanStatus.ISSUED)
                .count();

        long overdueLoans = allLoans.stream()
                .filter(l -> l.getStatus() == LoanStatus.ISSUED && l.getDueDate().isBefore(today))
                .count();

        long returnedLoans = allLoans.stream()
                .filter(l -> l.getStatus() == LoanStatus.RETURNED)
                .count();

        // Recent 8 transactions
        List<Loan> recentActivity = loanRepository.findTop10ByOrderByCreatedAtDesc();

        stats.setTotalBooks(totalBooks);
        stats.setTotalCopies(totalCopies);
        stats.setAvailableCopies(availableCopies);
        stats.setIssuedCopies(issuedCopies);
        stats.setTotalMembers(totalMembers);
        stats.setActiveMembers(activeMembers);
        stats.setCurrentlyIssuedLoans(currentlyIssued);
        stats.setOverdueLoans(overdueLoans);
        stats.setReturnedLoans(returnedLoans);
        stats.setCategoryCounts(categoryCounts);
        stats.setRecentActivity(recentActivity);

        return stats;
    }
}

