package com.survey.service;

import com.survey.dto.UsersSurveyDTO;
import com.survey.entity.Survey;
import com.survey.entity.Users;
import com.survey.entity.UsersSurvey;
import com.survey.repository.SurveyRepository;
import com.survey.repository.UsersRepository;
import com.survey.repository.UsersSurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UsersSurveyService {
    @Autowired
    private UsersSurveyRepository usersSurveyRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UsersRepository usersRepository;

    // Convert DTO to Entity
    public UsersSurvey convertDTO(UsersSurveyDTO usersSurveyDTO) {
        UsersSurvey usersSurvey = new UsersSurvey();
        Survey survey = surveyRepository.findBySurveyId(usersSurveyDTO.getSurveyId());
        Users users = usersRepository.findByUsersId(usersSurveyDTO.getUsersId());
        if(usersSurveyDTO.getLogId() != null){
            usersSurvey.setLogId(usersSurveyDTO.getLogId());
        }
        usersSurvey.setSurveyId(survey);
        usersSurvey.setUsersId(users);
        usersSurvey.setSurveyDate(LocalDateTime.now());
        usersSurvey.setPointGiven(usersSurveyDTO.getPointGiven());
        return usersSurvey;
    }

    // Convert Entity to DTO
    public UsersSurveyDTO convertDTO(UsersSurvey usersSurvey) {
        UsersSurveyDTO usersSurveyDTO = new UsersSurveyDTO();
        usersSurveyDTO.setLogId(usersSurvey.getLogId());
        usersSurveyDTO.setSurveyId(usersSurvey.getSurveyId().getSurveyId());
        usersSurveyDTO.setUsersId(usersSurvey.getUsersId().getUsersId());
        usersSurveyDTO.setPointGiven(usersSurvey.getPointGiven());
        usersSurveyDTO.setSurveyDate(usersSurvey.getSurveyDate());
        return usersSurveyDTO;
    }

    // Create
    @Transactional
    public void save(UsersSurveyDTO usersSurveyDTO) {
        UsersSurvey usersSurvey = convertDTO(usersSurveyDTO);
        UsersSurvey saved = usersSurveyRepository.save(usersSurvey);
        log.info("SAVE COMPLETE, LogId : {}", saved.getLogId());
    }


    // Read
    // For customer
    public List<UsersSurveyDTO> findByUsersId(String usersId) {
        List<UsersSurveyDTO> usersSurveyDTOList = new ArrayList<>();
        List<UsersSurvey> usersSurveyList = usersSurveyRepository.findByUsersIdOrderBySurveyDateDesc(usersRepository.findByUsersId(usersId));
        for(UsersSurvey usersSurvey : usersSurveyList){
            UsersSurveyDTO usersSurveyDTO = convertDTO(usersSurvey);
            usersSurveyDTOList.add(usersSurveyDTO);
        }

        return usersSurveyDTOList;
    }

    // For Admin
    public List<UsersSurveyDTO> findAll() {
        List<UsersSurveyDTO> usersSurveyDTOList = new ArrayList<>();
        List<UsersSurvey> usersSurveyList = usersSurveyRepository.findAll();
        for(UsersSurvey usersSurvey : usersSurveyList){
            UsersSurveyDTO usersSurveyDTO = convertDTO(usersSurvey);
            usersSurveyDTOList.add(usersSurveyDTO);
        }

        return usersSurveyDTOList;
    }

    // Update
    @Transactional
    public void update(UsersSurveyDTO usersSurveyDTO) {
        UsersSurvey usersSurvey = convertDTO(usersSurveyDTO);
        UsersSurvey saved = usersSurveyRepository.save(usersSurvey);
        log.info("UPDATE COMPLETE, LogId : {}", saved.getLogId());
    }


    // Delete
    @Transactional
    public void delete(String logId) {
        UsersSurvey usersSurvey = usersSurveyRepository.findByLogId(logId);
        usersSurveyRepository.delete(usersSurvey);
        log.info("DELETE COMPLETE, LogId : {}", logId);
    }



}
