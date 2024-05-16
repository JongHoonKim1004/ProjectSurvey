package com.survey.service;

import com.survey.dto.NextQuestionDTO;
import com.survey.dto.OptionsDTO;
import com.survey.dto.QuestionDTO;
import com.survey.entity.Options;
import com.survey.entity.Question;
import com.survey.repository.OptionsRepository;
import com.survey.repository.QuestionRepository;
import com.survey.repository.SurveyRepository;
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
    @Autowired
    private SurveyRepository surveyRepository;

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
    public OptionsDTO save(OptionsDTO optionsDTO) {
        Options options = convertDTO(optionsDTO);
        Options savedOptions = optionsRepository.save(options);
        log.info("Options Saved: {}", savedOptions);

        return convertEntity(savedOptions);
    }

    // Save With NextQuestionDTO
    @Transactional
    public NextQuestionDTO saveWithNextQuestionDTO(NextQuestionDTO nextQuestionDTO) {
        // 반환할 DTO 설정
        NextQuestionDTO returnDTO = new NextQuestionDTO();
        // 질문 저장
        QuestionDTO questionDTO = nextQuestionDTO.getQuestion();
        QuestionDTO questionDTO1 = questionService.save(questionDTO);
        returnDTO.setQuestion(questionDTO1);
        log.info("Question Saved: {}", questionDTO1.getQuestionId());

        // 저장한 질문에서 선택지에 담을 질문 id 호출
        String questionId = questionDTO1.getQuestionId();

        // 선택지 저장
        List<OptionsDTO> optionsDTOList = nextQuestionDTO.getOptions();
        List<OptionsDTO> optionsDTOList1 = new ArrayList<>();
        for(OptionsDTO optionsDTO : optionsDTOList){
            optionsDTO.setQuestionId(questionId);
            OptionsDTO optionsDTO1 = save(optionsDTO);
            optionsDTOList1.add(optionsDTO1);
            log.info("Options Saved: {}", optionsDTO1.getOptionsId());
        }
        returnDTO.setOptions(optionsDTOList1);
        log.info("Options All Saved");

        return returnDTO;
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

    // Get NextQuestionDTO LIST
    public List<NextQuestionDTO> getNextQuestionList(String surveyId){
        List<NextQuestionDTO> nextQuestionDTOList = new ArrayList<>();
        List<QuestionDTO> questionDTOList = questionService.findBySurveyId(surveyId);

        for(QuestionDTO questionDTO : questionDTOList){
            NextQuestionDTO nextQuestionDTO = new NextQuestionDTO();
            List<Options> optionsList = optionsRepository.findByQuestionIdOrderByOptionsNumberAsc(questionRepository.findByQuestionId(questionDTO.getQuestionId()));
            List<OptionsDTO> optionsDTOList = new ArrayList<>();
            for(Options options : optionsList){
                optionsDTOList.add(convertEntity(options));
            }

            nextQuestionDTO.setOptions(optionsDTOList);
            nextQuestionDTO.setQuestion(questionDTO);
            nextQuestionDTOList.add(nextQuestionDTO);
        }

        return nextQuestionDTOList;
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
    public OptionsDTO update(OptionsDTO optionsDTO) {
        Options options = convertDTO(optionsDTO);
        Options saved= optionsRepository.save(options);
        log.info("Options Updated: {}", options.getOptionsId());

        return convertEntity(saved);
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
