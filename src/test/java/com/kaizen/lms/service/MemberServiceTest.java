package com.kaizen.lms.service;

import com.kaizen.lms.dto.MemberFormDto;
import com.kaizen.lms.entity.Member;
import com.kaizen.lms.enums.LoanStatus;
import com.kaizen.lms.enums.MemberStatus;
import com.kaizen.lms.exception.DuplicateResourceException;
import com.kaizen.lms.exception.InvalidLoanOperationException;
import com.kaizen.lms.exception.ResourceNotFoundException;
import com.kaizen.lms.repository.LoanRepository;
import com.kaizen.lms.repository.MemberRepository;
import com.kaizen.lms.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying MemberService business rules and constraints.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member sampleMember;
    private MemberFormDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleMember = new Member("MEM-2026-1001", "Aarav Sharma", "aarav@college.edu",
                "+91 98765 43210", LocalDate.now(), MemberStatus.ACTIVE);
        sampleMember.setId(1L);

        sampleDto = new MemberFormDto();
        sampleDto.setName("Aarav Sharma");
        sampleDto.setEmail("aarav@college.edu");
        sampleDto.setPhone("+91 98765 43210");
        sampleDto.setStatus(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("Successfully register a new member with valid data")
    void testRegisterMember_Success() {
        when(memberRepository.existsByEmail("aarav@college.edu")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member m = invocation.getArgument(0);
            m.setId(5L);
            return m;
        });

        Member saved = memberService.registerMember(sampleDto);

        assertNotNull(saved);
        assertEquals(5L, saved.getId());
        assertEquals("Aarav Sharma", saved.getName());
        assertTrue(saved.getMembershipNumber().startsWith("MEM-"));
        assertEquals(MemberStatus.ACTIVE, saved.getStatus());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("Reject registration when email address is duplicated")
    void testRegisterMember_DuplicateEmail_ThrowsException() {
        when(memberRepository.existsByEmail("aarav@college.edu")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> memberService.registerMember(sampleDto));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("Prevent deleting a member who has active unreturned loans")
    void testDeleteMember_WithActiveLoans_ThrowsException() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));
        when(loanRepository.existsByMemberIdAndStatus(1L, LoanStatus.ISSUED)).thenReturn(true);

        assertThrows(InvalidLoanOperationException.class, () -> memberService.deleteMember(1L));
        verify(memberRepository, never()).delete(any(Member.class));
    }

    @Test
    @DisplayName("Successfully delete a member who has no active loans")
    void testDeleteMember_NoActiveLoans_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));
        when(loanRepository.existsByMemberIdAndStatus(1L, LoanStatus.ISSUED)).thenReturn(false);

        assertDoesNotThrow(() -> memberService.deleteMember(1L));
        verify(memberRepository, times(1)).delete(sampleMember);
    }

    @Test
    @DisplayName("Filter active members using Java Streams")
    void testGetActiveMembers_FiltersCorrectly() {
        Member m1 = new Member("MEM-1", "User 1", "u1@test.com", "111", LocalDate.now(), MemberStatus.ACTIVE);
        Member m2 = new Member("MEM-2", "User 2", "u2@test.com", "222", LocalDate.now(), MemberStatus.SUSPENDED);
        Member m3 = new Member("MEM-3", "User 3", "u3@test.com", "333", LocalDate.now(), MemberStatus.ACTIVE);

        when(memberRepository.findAll()).thenReturn(Arrays.asList(m1, m2, m3));

        List<Member> activeList = memberService.getActiveMembers();

        assertEquals(2, activeList.size());
        assertTrue(activeList.contains(m1));
        assertFalse(activeList.contains(m2));
        assertTrue(activeList.contains(m3));
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when member ID is not found")
    void testGetMemberById_NotFound() {
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberById(999L));
    }
}

