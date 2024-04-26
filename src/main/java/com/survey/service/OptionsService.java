package com.survey.service;

import com.survey.dto.NextQuestionDTO;
import com.survey.dto.OptionsDTO;
import com.survey.dto.QuestionDTO;
import com.survey.entity.Options;
import com.survey.entity.Question;
import com.survey.repository.OptionsRepository;
import com.survey.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OptionsService {
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    // Convert DTO to Entity
    public Options convertDTO(OptionsDTO optionsDTO) {
        Options options = new Options();
        if(optionsDTO.getOptionsId() != null){
            options.setOptionsId(optionsDTO.getOptionsId());
        }
        options.setQuestionId(questionRepository.findByQuestionId(optionsDTO.getQuestionId()));
        options.setOptionsNumber(optionsDTO.getOptionsNumber());
        options.setOptions(optionsDTO.getOptions());
        options.setTerminate(optionsDTO.isTerminate());
        return options;
    }

    // Convert Entity to DTO
    public OptionsDTO convertEntity(Options options) {
        OptionsDTO optionsDTO = new OptionsDTO();
        optionsDTO.setOptionsId(options.getOptionsId());
        optionsDTO.setQuestionId(options.getQuestionId().getQuestionId());
        optionsDTO.setOptionsNumber(options.getOptionsNumber());
        optionsDTO.setOptions(options.getOptions());
        optionsDTO.setTerminate(options.isTerminate());
        return optionsDTO;
    }

    // Create
    @Transactional
    public void save(OptionsDTO optionsDTO) {
        Options options = convertDTO(optionsDTO);
        Options savedOptions = optionsRepository.save(options);
        log.info("Options Saved: {}", savedOptions.getOptionsId());
    }

    // Save With NextQuestionDTO
    @Transactional
    public void saveWithNextQuestionDTO(NextQuestionDTO nextQuestionDTO) {
        // 질문 저장
        QuestionDTO questionDTO = nextQuestionDTO.getQuestion();
        questionService.save(questionDTO);
        log.info("Question Saved: {}", questionDTO.getQuestionId());

        // 선택지 저장
        List<OptionsDTO> optionsDTOList = nextQuestionDTO.getOptions();
        for(OptionsDTO optionsDTO : optionsDTOList){
            optionsRepository.save(convertDTO(optionsDTO));
            log.info("Options Saved: {}", optionsDTO.getOptionsId());
        }
        log.info("Options All Saved");
    }

    // Read
    // Get NextQuestion
    public NextQuestionDTO getNextQuestion(String questionId){
        NextQuestionDTO nextQuestionDTO = new NextQuestionDTO();
        Question question = questionRepository.findByQuestionId(questionId);
        QuestionDTO questionDTO = questionService.convertQuestion(question);
        nextQuestionDTO.setQuestion(questionDTO);
        List<Options> optionsList = optionsRepository.findByQuestionIdOrderByOptionsNumberAsc(questionRepository.findByQuestionId(questionId));
        List<OptionsDTO> optionsDTOList = new ArrayList<>();
        for(Options options : optionsList){
            optionsDTOList.add(convertEntity(options));
        }
        nextQuestionDTO.setOptions(optionsDTOList);
        return nextQuestionDTO;
    }

    // Get OneByOptionsId
    public OptionsDTO findByOptionsId(String optionsId) {
        Options options = optionsRepository.findByOptionsId(optionsId);
        OptionsDTO optionsDTO = convertEntity(options);

        return optionsDTO;
    }

    // Get List By QuestionId
    public List<OptionsDTO> findByQuestionId(String questionId) {
        List<Options> optionsList = optionsRepository.findByQuestionIdOrderByOptionsNumberAsc(questionRepository.findByQuestionId(questionId));
        List<OptionsDTO> optionsDTOList = new ArrayList<>();
        for(Options options : optionsList){
            optionsDTOList.add(convertEntity(options));
        }

        return optionsDTOList;
    }

    // Update
    @Transactional
    public void update(OptionsDTO optionsDTO) {
        Options options = convertDTO(optionsDTO);
        optionsRepository.save(options);
        log.info("Options Updated: {}", options.getOptionsId());
    }

    // Delete
    @Transactional
    public void delete(String optionsId) {
        Options options = optionsRepository.findByOptionsId(optionsId);
        optionsRepository.delete(options);
        log.info("Options Deleted: {}", options.getOptionsId());
    }

    // Check terminate
    public boolean CheckTerminate(String optionsId) {
        return optionsRepository.findByOptionsId(optionsId).isTerminate();
    }

}
