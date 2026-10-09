package com.example.demo.service;

import com.example.demo.dto.MemberFormDto;
import com.example.demo.entity.Member;

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

