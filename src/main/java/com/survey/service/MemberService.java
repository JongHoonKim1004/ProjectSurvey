package com.survey.service;

import com.survey.dto.MemberDTO;
import com.survey.entity.Member;
import com.survey.entity.Users;
import com.survey.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    // Convert DTO to Entity
    public Member convertDTO(MemberDTO memberDTO) {
        Member member = new Member();
        if(memberDTO.getMemberId() != null){
            member.setMemberId(memberDTO.getMemberId());
        }
        member.setName(memberDTO.getName());
        member.setNickname(memberDTO.getNickname());
        member.setPassword(memberDTO.getPassword());
        member.setAddr(memberDTO.getAddr());
        member.setAddrDetail(memberDTO.getAddrDetail());
        member.setZipNo(memberDTO.getZipNo());
        member.setPhone(memberDTO.getPhone());
        member.setEstDate(memberDTO.getEstDate());
        member.setCompNo(memberDTO.getCompNo());

        return member;
    }

    // Convert Entity to DTO
    public MemberDTO convertMember(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setName(member.getName());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setAddr(member.getAddr());
        memberDTO.setAddrDetail(member.getAddrDetail());
        memberDTO.setZipNo(member.getZipNo());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setEstDate(member.getEstDate());
        memberDTO.setCompNo(member.getCompNo());

        return memberDTO;
    }

    // Create
    @Transactional
    public MemberDTO save(MemberDTO memberDTO) {
        Member member = convertDTO(memberDTO);
        Member saved = memberRepository.save(member);
        log.info("SAVE COMPLETE, MEMBER ID : {}", saved.getMemberId());
        return convertMember(saved);
    }

    // Read
    // For Member
    public MemberDTO findByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        MemberDTO memberDTO = convertMember(member);

        return memberDTO;
    }

    // For Admin
    public List<MemberDTO> findAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberDTO> memberDTOs = new ArrayList<>();
        for(Member member : members){
            MemberDTO memberDTO = convertMember(member);
            memberDTOs.add(memberDTO);
        }

        return memberDTOs;
    }

    // Find By name
    public MemberDTO findByName(String name){
        Member member = memberRepository.findByName(name);
        return member != null ?  convertMember(member) : null ;
    }

    // Update
    @Transactional
    public void update(MemberDTO memberDTO) {
        Member member = convertDTO(memberDTO);
        Member saved = memberRepository.save(member);
        log.info("UPDATE COMPLETE, MEMBER ID : {}", saved.getMemberId());

    }
    // Delete
    @Transactional
    public void delete(String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        memberRepository.delete(member);
        log.info("DELETE COMPLETE, MEMBER ID : {}", member.getMemberId());
    }

    // 로그인 관련
    // User check
    public Member getByCredentials(final String name, final String password, final PasswordEncoder passwordEncoder) {
        final Member originalUser = memberRepository.findByName(name);

        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }

        return null;
    }
}
