package com.survey.service;

import com.survey.dto.SurveyDTO;
import com.survey.entity.Member;
import com.survey.entity.Survey;
import com.survey.repository.MemberRepository;
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
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private MemberRepository memberRepository;

    // Convert DTO to Entity
    public Survey convertDTO(SurveyDTO surveyDTO) {
        Survey survey = new Survey();
        if(surveyDTO.getSurveyId() != null) {
            survey.setSurveyId(surveyDTO.getSurveyId());
        }
        survey.setName(surveyDTO.getName());
        survey.setDescription(surveyDTO.getDescription());
        survey.setSurveyMember(memberRepository.findByMemberId(surveyDTO.getSurveyMember()));
        survey.setPoint(surveyDTO.getPoint());
        survey.setPointAtLeast(surveyDTO.getPointAtLeast());
        if(surveyDTO.getRegDate() != null){
            survey.setRegDate(surveyDTO.getRegDate());
        } else {
            survey.setRegDate(LocalDateTime.now());
        }
        survey.setStartDate(surveyDTO.getStartDate());
        survey.setEndDate(surveyDTO.getEndDate());
        survey.setSurveyParticipate(surveyDTO.getSurveyParticipate());

        return survey;
    }

    // Convert Entity to DTO
    public SurveyDTO convertSurvey(Survey survey) {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setSurveyId(survey.getSurveyId());
        surveyDTO.setName(survey.getName());
        surveyDTO.setDescription(survey.getDescription());
        surveyDTO.setSurveyMember(survey.getSurveyMember().getMemberId());
        surveyDTO.setPoint(survey.getPoint());
        surveyDTO.setPointAtLeast(survey.getPointAtLeast());
        surveyDTO.setRegDate(survey.getRegDate());
        surveyDTO.setStartDate(survey.getStartDate());
        surveyDTO.setEndDate(survey.getEndDate());
        surveyDTO.setSurveyParticipate(survey.getSurveyParticipate());
        return surveyDTO;
    }

    // Create
    @Transactional
    public void save(SurveyDTO surveyDTO){
        Survey survey = convertDTO(surveyDTO);
        Survey savedSurvey = surveyRepository.save(survey);
        log.info("SAVED SURVEY: {}", savedSurvey.getSurveyId());
    }

    // Read
    // Get ListAll
    public List<SurveyDTO> findAll(){
        List<Survey> surveys = surveyRepository.findAll();
        List<SurveyDTO> surveyDTOs = new ArrayList<>();
        for (Survey survey : surveys) {
            surveyDTOs.add(convertSurvey(survey));
        }
        return surveyDTOs;
    }

    // Get ListByMember
    public List<SurveyDTO> findByMemberId(String MemberId){
        Member member = memberRepository.findByMemberId(MemberId);
        List<Survey> surveyList = surveyRepository.findBySurveyMember(memberRepository.findByMemberId(MemberId));
        List<SurveyDTO> surveyDTOs = new ArrayList<>();
        for (Survey survey : surveyList) {
            surveyDTOs.add(convertSurvey(survey));
        }
        return surveyDTOs;
    }

    // Get One
    public SurveyDTO findBySurveyId(String surveyId){
        Survey survey = surveyRepository.findBySurveyId(surveyId);
        return convertSurvey(survey);
    }

    // Update
    @Transactional
    public void update(SurveyDTO surveyDTO){
        Survey survey = convertDTO(surveyDTO);
        Survey savedSurvey = surveyRepository.save(survey);
        log.info("UPDATED SURVEY: {}", savedSurvey.getSurveyId());
    }

    // Delete
    @Transactional
    public void delete(String surveyId){
        Survey survey = surveyRepository.findBySurveyId(surveyId);
        surveyRepository.delete(survey);
        log.info("DELETED SURVEY: {}", survey.getSurveyId());
    }

    // +1 surveyParticipate
    @Transactional
    public SurveyDTO plusSurveyParticipate(SurveyDTO surveyDTO){
        Survey survey = surveyRepository.findBySurveyId(surveyDTO.getSurveyId());
        int participate = survey.getSurveyParticipate();
        survey.setSurveyParticipate(participate + 1);
        Survey saved = surveyRepository.save(survey);
        return convertSurvey(survey);
    }
}
