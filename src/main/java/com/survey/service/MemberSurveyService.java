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
        memberSurvey.setUsersId(memberSurveyDTO.getUsersId());
        memberSurvey.setPointGiven(memberSurveyDTO.getPointGiven());
        if(memberSurveyDTO.getSurveyTime() != null){
            memberSurvey.setSurveyTime(memberSurveyDTO.getSurveyTime());
        } else {
            memberSurvey.setSurveyTime(LocalDateTime.now());
        }

        return memberSurvey;
    }

    // Convert Entity to DTO
    public MemberSurveyDTO convertEntity(MemberSurvey memberSurvey) {
        MemberSurveyDTO memberSurveyDTO = new MemberSurveyDTO();
        memberSurveyDTO.setLogId(memberSurvey.getLogId());
        memberSurveyDTO.setSurveyId(memberSurvey.getSurveyId().getSurveyId());
        memberSurveyDTO.setMemberId(memberSurvey.getMemberId().getMemberId());
        memberSurveyDTO.setUsersId(memberSurvey.getUsersId());
        memberSurveyDTO.setPointGiven(memberSurvey.getPointGiven());
        memberSurveyDTO.setSurveyTime(memberSurvey.getSurveyTime());

        return memberSurveyDTO;
    }

    // Create
    @Transactional
    public MemberSurveyDTO save(MemberSurveyDTO memberSurveyDTO) {
        MemberSurvey memberSurvey = convertDTO(memberSurveyDTO);
        MemberSurvey saved = memberSurveyRepository.save(memberSurvey);
        log.info("SAVED COMPLETE, ID : {}",saved);

        return convertEntity(saved);
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

    // For Member one survey
    public List<MemberSurveyDTO> findBySurveyId(String surveyId){
        List<MemberSurveyDTO> memberSurveyDTOList = new ArrayList<>();
        List<MemberSurvey> memberSurveyList = memberSurveyRepository.findBySurveyId(surveyRepository.findBySurveyId(surveyId));
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
