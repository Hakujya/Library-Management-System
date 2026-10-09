package com.kaizen.lms.controller;

import com.kaizen.lms.dto.LoanIssueFormDto;
import com.kaizen.lms.entity.Book;
import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.exception.BookNotAvailableException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.service.BookService;
import com.kaizen.lms.service.LoanService;
import com.kaizen.lms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller handling loan management, book issuing, and book returns.
 */
@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final MemberService memberService;

    public LoanController(LoanService loanService,
                          BookService bookService,
                          MemberService memberService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listLoans(@RequestParam(value = "tab", defaultValue = "active") String tab, Model model) {
        List<Loan> loans;
        switch (tab.toLowerCase()) {
            case "overdue":
                loans = loanService.getOverdueLoans();
                break;
            case "returned":
                loans = loanService.getReturnedLoans();
                break;
            case "all":
                loans = loanService.getAllLoans();
                break;
            case "active":
            default:
                loans = loanService.getActiveLoans();
                tab = "active";
                break;
        }

        model.addAttribute("loans", loans);
        model.addAttribute("currentTab", tab);
        model.addAttribute("activeCount", loanService.getActiveLoans().size());
        model.addAttribute("overdueCount", loanService.getOverdueLoans().size());
        model.addAttribute("returnedCount", loanService.getReturnedLoans().size());
        model.addAttribute("totalCount", loanService.getAllLoans().size());
        model.addAttribute("activeNav", "loans");
        return "loans/list";
    }

    @GetMapping("/issue")
    public String showIssueForm(@RequestParam(value = "bookId", required = false) Long bookId,
                                @RequestParam(value = "memberId", required = false) Long memberId,
                                Model model) {
        LoanIssueFormDto formDto = new LoanIssueFormDto();
        if (bookId != null) {
            formDto.setBookId(bookId);
        }
        if (memberId != null) {
            formDto.setMemberId(memberId);
        }

        populateIssueFormModel(model, formDto);
        model.addAttribute("activeNav", "loans");
        return "loans/issue-form";
    }

    @PostMapping("/issue")
    public String issueBook(@Valid @ModelAttribute("loan") LoanIssueFormDto formDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateIssueFormModel(model, formDto);
            model.addAttribute("activeNav", "loans");
            return "loans/issue-form";
        }

        try {
            Loan loan = loanService.issueBook(formDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Book '%s' successfully issued to '%s'. Due date: %s.",
                            loan.getBook().getTitle(), loan.getMember().getName(), loan.getDueDate()));
            return "redirect:/loans";
        } catch (BookNotAvailableException ex) {
            bindingResult.rejectValue("bookId", "error.loan", ex.getMessage());
            populateIssueFormModel(model, formDto);
            model.addAttribute("activeNav", "loans");
            return "loans/issue-form";
        } catch (InvalidLoanOperationException ex) {
            bindingResult.rejectValue("memberId", "error.loan", ex.getMessage());
            populateIssueFormModel(model, formDto);
            model.addAttribute("activeNav", "loans");
            return "loans/issue-form";
        } catch (Exception ex) {
            bindingResult.reject("error.loan", "Issue failed: " + ex.getMessage());
            populateIssueFormModel(model, formDto);
            model.addAttribute("activeNav", "loans");
            return "loans/issue-form";
        }
    }

    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable("id") Long id,
                             @RequestParam(value = "returnNotes", required = false) String returnNotes,
                             RedirectAttributes redirectAttributes) {
        try {
            Loan loan = loanService.returnBook(id, LocalDate.now(), returnNotes);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Book '%s' returned successfully from member '%s'. Copy restored to inventory.",
                            loan.getBook().getTitle(), loan.getMember().getName()));
        } catch (InvalidLoanOperationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing return: " + ex.getMessage());
        }
        return "redirect:/loans";
    }

    private void populateIssueFormModel(Model model, LoanIssueFormDto formDto) {
        List<Book> availableBooks = bookService.getAvailableBooks();
        List<Member> activeMembers = memberService.getActiveMembers();

        model.addAttribute("loan", formDto);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("activeMembers", activeMembers);
    }
}

