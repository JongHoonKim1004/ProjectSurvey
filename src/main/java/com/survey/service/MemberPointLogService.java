package com.survey.service;


import com.survey.dto.MemberPointDTO;
import com.survey.dto.MemberPointLogDTO;
import com.survey.entity.MemberPointLog;
import com.survey.repository.MemberPointLogRepository;
import com.survey.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(memberPointLogDTO.getLogId() != null){
            memberPointLog.setLogId(memberPointLogDTO.getLogId());
        }
        memberPointLog.setMemberId(memberRepository.findByMemberId(memberPointLogDTO.getMemberId()));
        memberPointLog.setPointChange(memberPointLogDTO.getPointChange());
        memberPointLog.setChangeType(memberPointLogDTO.getChangeType());
        memberPointLog.setChangeDate(memberPointLogDTO.getChangeDate());

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
    @Transactional
    public void save(MemberPointLogDTO memberPointLogDTO) {
        MemberPointLog memberPointLog = convertDTO(memberPointLogDTO);
        MemberPointLog saved = memberPointLogRepository.save(memberPointLog);
        log.info("SAVED COMPLETE, ID : {}", saved.getLogId());
    }
    // Read
    // For Member
    public List<MemberPointLogDTO> findByMemberId(String memberId) {
        List<MemberPointLog> memberPointLogList = memberPointLogRepository.findByMemberId(memberRepository.findByMemberId(memberId));
        List<MemberPointLogDTO> memberPointLogDTOList = new ArrayList<>();
        for (MemberPointLog memberPointLog : memberPointLogList){
            MemberPointLogDTO memberPointDTO = convertEntity(memberPointLog);
            memberPointLogDTOList.add(memberPointDTO);
        }

        return memberPointLogDTOList;
    }

    // For Admin
    public List<MemberPointLogDTO> findAll(){
        List<MemberPointLog> memberPointLogList = memberPointLogRepository.findAll();
        List<MemberPointLogDTO> memberPointLogDTOList = new ArrayList<>();
        for(MemberPointLog memberPointLog : memberPointLogList){
            MemberPointLogDTO memberPointLogDTO = convertEntity(memberPointLog);
            memberPointLogDTOList.add(memberPointLogDTO);
        }

        return memberPointLogDTOList;
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
    public void delete(String logId){
        MemberPointLog memberPointLog = memberPointLogRepository.findByLogId(logId);
        memberPointLogRepository.delete(memberPointLog);
        log.info("DELETE COMPLETE, ID: {}", memberPointLog.getLogId());
    }
}
