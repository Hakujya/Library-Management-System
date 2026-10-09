package com.kaizen.lms.controller;

import com.kaizen.lms.dto.MemberFormDto;
import com.kaizen.lms.entity.Loan;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.exception.DuplicateResourceException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.service.LoanService;
import com.kaizen.lms.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller handling member registration, profile management, and history.
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final LoanService loanService;

    public MemberController(MemberService memberService, LoanService loanService) {
        this.memberService = memberService;
        this.loanService = loanService;
    }

    @GetMapping
    public String listMembers(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Member> members;
        if (search != null && !search.trim().isEmpty()) {
            members = memberService.searchMembers(search);
        } else {
            members = memberService.getAllMembers();
        }

        model.addAttribute("members", members);
        model.addAttribute("searchTerm", search);
        model.addAttribute("activeNav", "members");
        return "members/list";
    }

    @GetMapping("/new")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new MemberFormDto());
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Register New Member");
        model.addAttribute("activeNav", "members");
        return "members/form";
    }

    @PostMapping
    public String registerMember(@Valid @ModelAttribute("member") MemberFormDto formDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Register New Member");
            model.addAttribute("activeNav", "members");
            return "members/form";
        }

        try {
            Member registered = memberService.registerMember(formDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Member '%s' (%s) registered successfully.",
                            registered.getName(), registered.getMembershipNumber()));
            return "redirect:/members/" + registered.getId();
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("email", "error.member", ex.getMessage());
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Register New Member");
            model.addAttribute("activeNav", "members");
            return "members/form";
        }
    }

    @GetMapping("/{id}")
    public String showMemberDetails(@PathVariable("id") Long id, Model model) {
        Member member = memberService.getMemberById(id);
        List<Loan> memberLoans = loanService.getLoansByMember(id);

        model.addAttribute("member", member);
        model.addAttribute("loans", memberLoans);
        model.addAttribute("activeNav", "members");
        return "members/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", memberService.toDto(member));
        model.addAttribute("statuses", MemberStatus.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("pageTitle", "Edit Member Profile");
        model.addAttribute("memberId", id);
        model.addAttribute("activeNav", "members");
        return "members/form";
    }

    @PostMapping("/{id}/edit")
    public String updateMember(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("member") MemberFormDto formDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Member Profile");
            model.addAttribute("memberId", id);
            model.addAttribute("activeNav", "members");
            return "members/form";
        }

        try {
            Member updated = memberService.updateMember(id, formDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Member '%s' updated successfully.", updated.getName()));
            return "redirect:/members/" + id;
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("email", "error.member", ex.getMessage());
            model.addAttribute("statuses", MemberStatus.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Member Profile");
            model.addAttribute("memberId", id);
            model.addAttribute("activeNav", "members");
            return "members/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.getMemberById(id);
            String name = member.getName();
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Member '%s' deleted successfully.", name));
        } catch (InvalidLoanOperationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete member: " + ex.getMessage());
        }
        return "redirect:/members";
    }
}

