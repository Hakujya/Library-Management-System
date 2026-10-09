package com.example.demo.service.impl;

import com.example.demo.dto.LoanIssueFormDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Member;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.MemberStatus;
import com.example.demo.exception.BookNotAvailableException;
import com.example.demo.exception.InvalidLoanOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.LoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of LoanService guaranteeing ACID transactional semantics
 * for inventory updates, issue validations, and return processing.
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanServiceImpl(LoanRepository loanRepository,
                           BookRepository bookRepository,
                           MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Loan issueBook(LoanIssueFormDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", dto.getBookId()));

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", dto.getMemberId()));

        // Check if member is active
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new InvalidLoanOperationException(String.format(
                    "Cannot issue books to member '%s' with status '%s'. Only active members can borrow books.",
                    member.getName(), member.getStatus().getDisplayName()));
        }

        // Validate book stock availability
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(book.getTitle(), book.getId());
        }

        // Validate dates
        LocalDate issueDate = dto.getIssueDate() != null ? dto.getIssueDate() : LocalDate.now();
        LocalDate dueDate = dto.getDueDate() != null ? dto.getDueDate() : issueDate.plusDays(14);

        if (dueDate.isBefore(issueDate)) {
            throw new InvalidLoanOperationException("Due date cannot be earlier than issue date.");
        }

        // Decrement book inventory
        book.decrementAvailableCopies();
        bookRepository.save(book);

        // Create new loan record
        Loan loan = new Loan(book, member, issueDate, dueDate);
        loan.setNotes(dto.getNotes() != null ? dto.getNotes().trim() : null);

        return loanRepository.save(loan);
    }

    @Override
    public Loan returnBook(Long loanId, LocalDate returnDate, String notes) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", loanId));

        // Prevent returning the same loan twice
        if (loan.isReturned()) {
            throw new InvalidLoanOperationException(String.format(
                    "Loan #%d for '%s' was already returned on %s.",
                    loan.getId(), loan.getBook().getTitle(), loan.getReturnDate()));
        }

        LocalDate actualReturnDate = (returnDate != null) ? returnDate : LocalDate.now();
        loan.markReturned(actualReturnDate);

        if (notes != null && !notes.trim().isEmpty()) {
            String updatedNotes = (loan.getNotes() != null && !loan.getNotes().isEmpty())
                    ? loan.getNotes() + " | Return note: " + notes.trim()
                    : notes.trim();
            loan.setNotes(updatedNotes);
        }

        // Restore book inventory
        Book book = loan.getBook();
        book.incrementAvailableCopies();
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getActiveLoans() {
        // Demonstrating Java Streams with filter, sorted, and collect
        return loanRepository.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.ISSUED)
                .sorted(Comparator.comparing(Loan::getDueDate))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getOverdueLoans() {
        // Demonstrating Java Streams with filter and sorted
        LocalDate today = LocalDate.now();
        return loanRepository.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.ISSUED && l.getDueDate().isBefore(today))
                .sorted(Comparator.comparing(Loan::getDueDate))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getReturnedLoans() {
        // Demonstrating Java Streams with filter
        return loanRepository.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.RETURNED)
                .sorted(Comparator.comparing(Loan::getReturnDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansByMember(Long memberId) {
        return loanRepository.findByMemberIdOrderByIssueDateDesc(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansByBook(Long bookId) {
        return loanRepository.findByBookIdOrderByIssueDateDesc(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> getRecentLoans(int limit) {
        return loanRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}

