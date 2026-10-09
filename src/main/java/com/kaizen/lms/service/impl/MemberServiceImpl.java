package com.kaizen.lms.service.impl;

import com.kaizen.lms.dto.MemberFormDto;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.exception.DuplicateResourceException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.exception.ResourceNotFoundException;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.repository.MemberRepository;
import com.kaizen.lms.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of MemberService handling member registration, validations,
 * uniqueness constraints, and safe deletions.
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public MemberServiceImpl(MemberRepository memberRepository, LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> searchMembers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMembers();
        }
        return memberRepository.searchMembers(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> getActiveMembers() {
        // Demonstrates Java Streams filter and collect
        return memberRepository.findAll().stream()
                .filter(Member::isActive)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
    }

    @Override
    public Member registerMember(MemberFormDto dto) {
        String cleanEmail = dto.getEmail().trim().toLowerCase();
        if (memberRepository.existsByEmail(cleanEmail)) {
            throw new DuplicateResourceException("email", cleanEmail);
        }

        String membershipNumber = dto.getMembershipNumber();
        if (membershipNumber == null || membershipNumber.trim().isEmpty()) {
            // Generate standard membership format: MEM-YYYY-XXXX
            membershipNumber = "MEM-" + LocalDate.now().getYear() + "-" +
                    UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        } else {
            membershipNumber = membershipNumber.trim();
            if (memberRepository.existsByMembershipNumber(membershipNumber)) {
                throw new DuplicateResourceException("membership number", membershipNumber);
            }
        }

        Member member = new Member();
        member.setMembershipNumber(membershipNumber);
        member.setName(dto.getName().trim());
        member.setEmail(cleanEmail);
        member.setPhone(dto.getPhone().trim());
        member.setRegistrationDate(dto.getRegistrationDate() != null ? dto.getRegistrationDate() : LocalDate.now());
        member.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.ACTIVE);
        member.setNotes(dto.getNotes() != null ? dto.getNotes().trim() : null);

        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long id, MemberFormDto dto) {
        Member member = getMemberById(id);
        String cleanEmail = dto.getEmail().trim().toLowerCase();

        if (memberRepository.existsByEmailAndIdNot(cleanEmail, id)) {
            throw new DuplicateResourceException("email", cleanEmail);
        }

        member.setName(dto.getName().trim());
        member.setEmail(cleanEmail);
        member.setPhone(dto.getPhone().trim());
        if (dto.getRegistrationDate() != null) {
            member.setRegistrationDate(dto.getRegistrationDate());
        }
        if (dto.getStatus() != null) {
            member.setStatus(dto.getStatus());
        }
        member.setNotes(dto.getNotes() != null ? dto.getNotes().trim() : null);

        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = getMemberById(id);

        // Enforce safety rule: Cannot delete member with active loans
        boolean hasActiveLoans = loanRepository.existsByMemberIdAndStatus(id, LoanStatus.ISSUED);
        if (hasActiveLoans) {
            throw new InvalidLoanOperationException(String.format(
                    "Cannot delete member '%s' because they currently have unreturned books on loan.",
                    member.getName()));
        }

        memberRepository.delete(member);
    }

    @Override
    public MemberFormDto toDto(Member member) {
        MemberFormDto dto = new MemberFormDto();
        dto.setId(member.getId());
        dto.setMembershipNumber(member.getMembershipNumber());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setRegistrationDate(member.getRegistrationDate());
        dto.setStatus(member.getStatus());
        dto.setNotes(member.getNotes());
        return dto;
    }
}

