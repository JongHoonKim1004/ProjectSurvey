package com.survey.service;

import com.survey.dto.ResponseDTO;
import com.survey.entity.Options;
import com.survey.entity.Question;
import com.survey.entity.Response;
import com.survey.entity.Users;
import com.survey.repository.OptionsRepository;
import com.survey.repository.QuestionRepository;
import com.survey.repository.ResponseRepository;
import com.survey.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResponseService {
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private UsersRepository usersRepository;

    // Convert DTO to Entities
    public List<Response> convertDTO(ResponseDTO dto) {
        List<Response> responses = new ArrayList<>();
        Question question = questionRepository.findById(dto.getQuestionId()).orElseThrow();
        Users user = usersRepository.findById(dto.getUsersId()).orElseThrow();

        for (String optionId : dto.getOptionsId()) {
            Options options = optionsRepository.findById(optionId).orElseThrow();

            Response response = new Response();
            if(dto.getResponseId() != null) {
                response.setResponseId(dto.getResponseId());
            }
            response.setQuestionId(question);
            response.setOptionsId(options);
            response.setUsersId(user);
            if(dto.getResponseText() != null){
                response.setResponseText(dto.getResponseText());
            }
            responses.add(response);
        }

        return responses;
    }

    // Convert Entity to DTO
    public ResponseDTO convertEntity(Response response) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(response.getResponseId());
        responseDTO.setQuestionId(response.getQuestionId().getQuestionId());
        List<String> optionsIds = new ArrayList<>();
        optionsIds.add(response.getOptionsId().getOptionsId());
        responseDTO.setOptionsId(optionsIds);
        responseDTO.setUsersId(response.getUsersId().getUsersId());
        responseDTO.setResponseText(response.getResponseText());
        return responseDTO;
    }

    // Create
    @Transactional
    public boolean saveResponses(List<ResponseDTO> responseDTOS) {
        boolean isTerminated = false;

        for(ResponseDTO dto : responseDTOS){
            Question question = questionRepository.findByQuestionId(dto.getQuestionId());

            Users user = usersRepository.findByUsersId(dto.getUsersId());

            for(String optionsId : dto.getOptionsId()){
                Options option = optionsRepository.findByOptionsId(optionsId);
                log.info("Option Setting : {}", option.toString());

                Response response = Response.builder().
                        questionId(question).
                        optionsId(option).
                        usersId(user).build();

                if(dto.getResponseText() != null){
                    response.setResponseText(dto.getResponseText());
                }

                responseRepository.save(response);

                if(option.isTerminate()){
                    isTerminated = true;
                }

            }
        }

        return isTerminated;
    }

    // Read
    // Get ListByQuestionId
    public List<ResponseDTO> findByQuestionId(String questionId) {

        List<ResponseDTO> responseDTOs = new ArrayList<>();
        Question question = questionRepository.findByQuestionId(questionId);
        List<Response> responses = responseRepository.findByQuestionId(question);
        for(Response response : responses){
            ResponseDTO responseDTO = convertEntity(response);
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    // Get OneBy QuestionId and OptionsId
    public ResponseDTO findByQuestionIdAndOptionsId(String questionId, String optionsId) {
        Question question = questionRepository.findByQuestionId(questionId);
        Options options = optionsRepository.findByOptionsId(optionsId);
        Response response = responseRepository.findByQuestionIdAndOptionsId(question, options);

        ResponseDTO responseDTO = convertEntity(response);
        return responseDTO;
    }

    // Update


    // Delete
    @Transactional
    public void delete(String responseId) {
        Response response = responseRepository.findByResponseId(responseId);
        responseRepository.delete(response);
        log.info("Response Deleted: {}", response.getResponseId());
    }


}
