package com.survey.service;

import com.survey.dto.ResponseDTO;
import com.survey.entity.Options;
import com.survey.entity.Question;
import com.survey.entity.Response;
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

    // Convert DTO to Entity
    public Response convertDTO(ResponseDTO dto) {
        Response response = new Response();
        if(dto.getResponseId() != null) {
            response.setResponseId(dto.getResponseId());
        }
        response.setQuestionId(questionRepository.findByQuestionId(dto.getQuestionId()));
        response.setOptionsId(optionsRepository.findByOptionsId(dto.getOptionsId()));
        response.setUsersId(usersRepository.findByUsersId(dto.getUsersId()));
        if(dto.getResponseText() != null){
            response.setResponseText(dto.getResponseText());
        }
        return response;
    }

    // Convert Entity to DTO
    public ResponseDTO convertEntity(Response response) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(response.getResponseId());
        responseDTO.setQuestionId(response.getQuestionId().getQuestionId());
        responseDTO.setOptionsId(response.getOptionsId().getOptionsId());
        responseDTO.setUsersId(response.getUsersId().getUsersId());
        responseDTO.setResponseText(response.getResponseText());
        return responseDTO;
    }

    // Create
    @Transactional
    public void save(ResponseDTO responseDTO) {
        Response response = convertDTO(responseDTO);
        Response savedResponse = responseRepository.save(response);
        log.info("Response Saved: {}", savedResponse.getResponseId());
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
    @Transactional
    public void update(ResponseDTO responseDTO) {
        Response response = convertDTO(responseDTO);
        Response savedResponse = responseRepository.save(response);
        log.info("Response Updated: {}", savedResponse.getResponseId());
    }

    // Delete
    @Transactional
    public void delete(String responseId) {
        Response response = responseRepository.findByResponseId(responseId);
        responseRepository.delete(response);
        log.info("Response Deleted: {}", response.getResponseId());
    }


}
