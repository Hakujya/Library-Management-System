package com.kaizen.lms.service;

import com.kaizen.lms.dto.MemberFormDto;
import com.kaizen.lms.entity.Member;

import java.util.List;

/**
 * Service interface for member management operations.
 */
public interface MemberService {

    List<Member> getAllMembers();

    List<Member> searchMembers(String keyword);

    List<Member> getActiveMembers();

    Member getMemberById(Long id);

    Member registerMember(MemberFormDto dto);

    Member updateMember(Long id, MemberFormDto dto);

    void deleteMember(Long id);

    MemberFormDto toDto(Member member);
}

