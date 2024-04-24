package com.survey.service;

import com.survey.dto.MemberSurveyDTO;
import com.survey.entity.MemberSurvey;
import com.survey.repository.MemberRepository;
import com.survey.repository.MemberSurveyRepository;
import com.survey.repository.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberSurveyService {
    @Autowired
    private MemberSurveyRepository memberSurveyRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private MemberRepository memberRepository;

    // Convert DTO to Entity
    public MemberSurvey convertDTO(MemberSurveyDTO memberSurveyDTO) {
        MemberSurvey memberSurvey = new MemberSurvey();
        if(memberSurveyDTO.getLogId() != null){
            memberSurvey.setLogId(memberSurveyDTO.getLogId());
        }
        memberSurvey.setSurveyId(surveyRepository.findBySurveyId(memberSurveyDTO.getSurveyId()));
        memberSurvey.setMemberId(memberRepository.findByMemberId(memberSurveyDTO.getMemberId()));
        memberSurvey.setStartDate(memberSurveyDTO.getStartDate());
        // 등록일이 없는 신규 등록일 경우 변환 시간을 등록 시간으로 설정
        if(memberSurveyDTO.getRegDate() != null){
            memberSurvey.setRegDate(memberSurveyDTO.getRegDate());
        } else {
            memberSurvey.setRegDate(LocalDateTime.now());
        }
        memberSurvey.setEndDate(memberSurveyDTO.getEndDate());
        memberSurvey.setPointGive(memberSurveyDTO.getPointGive());
        memberSurvey.setPointAtLeastGive(memberSurveyDTO.getPointAtLeastGive());
        return memberSurvey;
    }

    // Convert Entity to DTO
    public MemberSurveyDTO convertEntity(MemberSurvey memberSurvey) {
        MemberSurveyDTO memberSurveyDTO = new MemberSurveyDTO();
        memberSurveyDTO.setLogId(memberSurvey.getLogId());
        memberSurveyDTO.setSurveyId(memberSurvey.getSurveyId().getSurveyId());
        memberSurveyDTO.setMemberId(memberSurvey.getMemberId().getMemberId());
        memberSurveyDTO.setStartDate(memberSurvey.getStartDate());
        memberSurveyDTO.setRegDate(memberSurvey.getRegDate());
        memberSurveyDTO.setEndDate(memberSurvey.getEndDate());
        memberSurveyDTO.setPointGive(memberSurvey.getPointGive());
        memberSurveyDTO.setPointAtLeastGive(memberSurvey.getPointAtLeastGive());

        return memberSurveyDTO;
    }

    // Create
    @Transactional
    public void save(MemberSurveyDTO memberSurveyDTO) {
        MemberSurvey memberSurvey = convertDTO(memberSurveyDTO);
        MemberSurvey saved = memberSurveyRepository.save(memberSurvey);
        log.info("SAVED COMPLETE, ID : {}",saved.getLogId());
    }

    // Read
    // For Member
    public List<MemberSurveyDTO> findByMemberID(String memberID) {
        List<MemberSurveyDTO> memberSurveyDTOList = new ArrayList<>();
        List<MemberSurvey> memberSurveyList = memberSurveyRepository.findByMemberId(memberRepository.findByMemberId(memberID));
        for(MemberSurvey memberSurvey : memberSurveyList){
            MemberSurveyDTO memberSurveyDTO = convertEntity(memberSurvey);
            memberSurveyDTOList.add(memberSurveyDTO);
        }
        return memberSurveyDTOList;
    }

    // For Admin
    public List<MemberSurveyDTO> findAll() {
        List<MemberSurvey> memberSurveys = memberSurveyRepository.findAll();
        List<MemberSurveyDTO> memberSurveyDTOs = new ArrayList<>();
        for(MemberSurvey memberSurvey : memberSurveys){
            MemberSurveyDTO memberSurveyDTO = convertEntity(memberSurvey);
            memberSurveyDTOs.add(memberSurveyDTO);
        }
        return memberSurveyDTOs;
    }
    // Update
    @Transactional
    public void update(MemberSurveyDTO memberSurveyDTO) {
        MemberSurvey memberSurvey = convertDTO(memberSurveyDTO);
        MemberSurvey saved = memberSurveyRepository.save(memberSurvey);
        log.info("UPDATED COMPLETE, ID : {}",saved.getLogId());
    }

    // Delete
    @Transactional
    public void delete(String logId) {
        MemberSurvey memberSurvey = memberSurveyRepository.findByLogId(logId);
        memberSurveyRepository.delete(memberSurvey);
        log.info("DELETED COMPLETE, ID : {}",memberSurvey.getLogId());
    }

}
