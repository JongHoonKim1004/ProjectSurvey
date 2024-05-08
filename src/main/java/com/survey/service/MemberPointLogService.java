package com.survey.service;


import com.survey.dto.MemberPointLogDTO;
import com.survey.entity.MemberPointLog;
import com.survey.repository.MemberPointLogRepository;
import com.survey.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberPointLogService {
    @Autowired
    private MemberPointLogRepository memberPointLogRepository;
    @Autowired
    private MemberRepository memberRepository;

    // Convert DTO to Entity
    public MemberPointLog convertDTO(MemberPointLogDTO memberPointLogDTO) {
        MemberPointLog memberPointLog = new MemberPointLog();
        if(memberPointLogDTO.getLogId() != 0){
            memberPointLog.setLogId(memberPointLogDTO.getLogId());
        }
        memberPointLog.setMemberId(memberRepository.findByMemberId(memberPointLogDTO.getMemberId()));
        memberPointLog.setPointChange(memberPointLogDTO.getPointChange());
        memberPointLog.setChangeType(memberPointLogDTO.getChangeType());
        if(memberPointLogDTO.getChangeDate() != null){
            memberPointLog.setChangeDate(memberPointLogDTO.getChangeDate());
        } else {
            memberPointLog.setChangeDate(LocalDateTime.now());
        }

        return memberPointLog;
    }

    // convert Entity to DTO
    public MemberPointLogDTO convertEntity(MemberPointLog memberPointLog) {
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setLogId(memberPointLog.getLogId());
        memberPointLogDTO.setMemberId(memberPointLog.getMemberId().getMemberId());
        memberPointLogDTO.setPointChange(memberPointLog.getPointChange());
        memberPointLogDTO.setChangeType(memberPointLog.getChangeType());
        memberPointLogDTO.setChangeDate(memberPointLog.getChangeDate());

        return memberPointLogDTO;
    }

    // Create
        // Normal
    @Transactional
    public MemberPointLogDTO save(MemberPointLogDTO memberPointLogDTO) {
        MemberPointLog memberPointLog = convertDTO(memberPointLogDTO);
        MemberPointLog saved = memberPointLogRepository.save(memberPointLog);
        log.info("SAVED COMPLETE, ID : {}", saved.getLogId());

        return convertEntity(saved);
    }
        // Create Plus Log
    @Transactional
    public MemberPointLogDTO savePlusLog(String memberId, Integer point, String changeType) {
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setMemberId(memberId);
        memberPointLogDTO.setPointChange(point);
        memberPointLogDTO.setChangeType(changeType);

        memberPointLogRepository.save(convertDTO(memberPointLogDTO));

        return memberPointLogDTO;
    }

        // Create Minus Log
    @Transactional
    public MemberPointLogDTO saveMinusLog(String memberId, Integer point, String changeType) {
        MemberPointLogDTO memberPointLogDTO = new MemberPointLogDTO();
        memberPointLogDTO.setMemberId(memberId);
        memberPointLogDTO.setPointChange(point * -1);
        memberPointLogDTO.setChangeType(changeType);

        memberPointLogRepository.save(convertDTO(memberPointLogDTO));

        return memberPointLogDTO;
    }

    // Read
    // For Member
    public List<MemberPointLogDTO> findByMemberId(String memberId) {
        List<MemberPointLog> memberPointLogList = memberPointLogRepository.findByMemberIdOrderByChangeDateDesc(memberRepository.findByMemberId(memberId));
        List<MemberPointLogDTO> memberPointLogDTOList = new ArrayList<>();
        for (MemberPointLog memberPointLog : memberPointLogList){
            MemberPointLogDTO memberPointDTO = convertEntity(memberPointLog);
            memberPointLogDTOList.add(memberPointDTO);
        }

        return memberPointLogDTOList;
    }

    // For Admin
    public List<MemberPointLogDTO> findAll(){
        List<MemberPointLog> memberPointLogList = memberPointLogRepository.findAllByOrderByChangeDateDesc();
        List<MemberPointLogDTO> memberPointLogDTOList = new ArrayList<>();
        for(MemberPointLog memberPointLog : memberPointLogList){
            MemberPointLogDTO memberPointLogDTO = convertEntity(memberPointLog);
            memberPointLogDTOList.add(memberPointLogDTO);
        }

        return memberPointLogDTOList;
    }

    // Get One
    public MemberPointLogDTO getOne(long logId){
        MemberPointLog memberPointLog = memberPointLogRepository.findByLogId(logId);
        MemberPointLogDTO memberPointLogDTO = convertEntity(memberPointLog);
        return memberPointLogDTO;
    }

    // Update
    @Transactional
    public void update(MemberPointLogDTO memberPointLogDTO){
        MemberPointLog memberPointLog = convertDTO(memberPointLogDTO);
        MemberPointLog updated = memberPointLogRepository.save(memberPointLog);
        log.info("UPDATE COMPLETE, ID: {}", updated.getLogId());
    }

    // Delete
    @Transactional
    public void delete(long logId){
        MemberPointLog memberPointLog = memberPointLogRepository.findByLogId(logId);
        memberPointLogRepository.delete(memberPointLog);
        log.info("DELETE COMPLETE, ID: {}", memberPointLog.getLogId());
    }
}
