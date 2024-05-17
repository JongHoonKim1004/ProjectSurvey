package com.survey.service;

import com.survey.dto.MemberPointDTO;
import com.survey.entity.MemberPoint;
import com.survey.repository.MemberPointRepository;
import com.survey.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberPointService {
    @Autowired
    private MemberPointRepository memberPointRepository;
    @Autowired
    private MemberRepository memberRepository;

    // Convert DTO to Entity
    public MemberPoint convertDTO(MemberPointDTO memberPointDTO) {
        MemberPoint memberPoint = new MemberPoint();
        if(memberPointDTO.getPointId() != null){
            memberPoint.setPointId(memberPointDTO.getPointId());
        }
        memberPoint.setMemberId(memberRepository.findByMemberId(memberPointDTO.getMemberId()));
        memberPoint.setPointTotal(memberPointDTO.getPointTotal());
        memberPoint.setPointUsed(memberPointDTO.getPointUsed());
        memberPoint.setPointBalance(memberPointDTO.getPointBalance());

        return memberPoint;
    }

    // Convert Entity to DTO
    public MemberPointDTO convertMemberPoint(MemberPoint memberPoint) {
        MemberPointDTO memberPointDTO = new MemberPointDTO();
        memberPointDTO.setPointId(memberPoint.getPointId());
        memberPointDTO.setMemberId(memberPoint.getMemberId().getMemberId());
        memberPointDTO.setPointTotal(memberPoint.getPointTotal());
        memberPointDTO.setPointUsed(memberPoint.getPointUsed());
        memberPointDTO.setPointBalance(memberPoint.getPointBalance());

        return memberPointDTO;
    }

    // Create
    @Transactional
    public MemberPointDTO save(MemberPointDTO memberPointDTO) {
        MemberPoint memberPoint = convertDTO(memberPointDTO);
        MemberPoint saved = memberPointRepository.save(memberPoint);
        log.info("SAVED COMPLETE, ID : {}", saved.getMemberId().getMemberId());

        return convertMemberPoint(saved);
    }

    // Read
    // For Member
    public MemberPointDTO findByMemberId(String memberId) {
        MemberPoint memberPoint = memberPointRepository.findByMemberId(memberRepository.findByMemberId(memberId));
        MemberPointDTO memberPointDTO = convertMemberPoint(memberPoint);

        return memberPointDTO;
    }

    // For Admin
    public List<MemberPointDTO> findAll() {
        List<MemberPoint> memberPoints = memberPointRepository.findAll();
        List<MemberPointDTO> memberPointDTOList = new ArrayList<>();
        for (MemberPoint memberPoint : memberPoints) {
            MemberPointDTO memberPointDTO = convertMemberPoint(memberPoint);
            memberPointDTOList.add(memberPointDTO);
        }

        return memberPointDTOList;
    }

    // Update
    @Transactional
    public MemberPointDTO update(MemberPointDTO memberPointDTO) {
        MemberPoint memberPoint = convertDTO(memberPointDTO);
        MemberPoint saved = memberPointRepository.save(memberPoint);
        log.info("UPDATED COMPLETE, ID : {}", saved.getMemberId().getMemberId());

        return convertMemberPoint(saved);
    }

    // Delete
    @Transactional
    public void delete(String pointId) {
        MemberPoint memberPoint = memberPointRepository.findByPointId(pointId);
        memberPointRepository.delete(memberPoint);
        log.info("DELETED COMPLETE, ID : {}", memberPoint.getPointId());
    }

    // Charge Point
    @Transactional
    public MemberPointDTO chargePoint(MemberPointDTO memberPointDTO, Integer point) {
        MemberPointDTO newMemberPointDTO = memberPointDTO;
        newMemberPointDTO.setPointTotal(memberPointDTO.getPointTotal() + point);
        newMemberPointDTO.setPointBalance(newMemberPointDTO.getPointBalance() + point);

        MemberPoint memberPoint = convertDTO(newMemberPointDTO);
        memberPointRepository.save(memberPoint);
        log.info("Giving point:{}, Balance : {}", point, newMemberPointDTO.getPointBalance());

        return newMemberPointDTO;
    }

    // Use Point
    @Transactional
    public MemberPointDTO usePoint(MemberPointDTO memberPointDTO, Integer point) {
        MemberPointDTO newMemberPointDTO = memberPointDTO;
        newMemberPointDTO.setPointUsed(memberPointDTO.getPointUsed() + point);
        newMemberPointDTO.setPointBalance(newMemberPointDTO.getPointBalance() - point);

        MemberPoint memberPoint = convertDTO(newMemberPointDTO);
        memberPointRepository.save(memberPoint);
        log.info("Used point:{}, Balance : {}", point, newMemberPointDTO.getPointBalance());

        return newMemberPointDTO;
    }
}
