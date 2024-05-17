package com.survey.service;

import com.survey.dto.QuestionDTO;
import com.survey.entity.Question;
import com.survey.entity.Survey;
import com.survey.repository.QuestionRepository;
import com.survey.repository.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SurveyRepository surveyRepository;

    // Constructor injection for SurveyRepository
    public QuestionService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    // Convert DTO to Entity
    public Question convertDTO(QuestionDTO questionDTO) {
        Question question = new Question();
        if(questionDTO.getQuestionId() != null){
            question.setQuestionId(questionDTO.getQuestionId());
        }
        question.setQuestionNumber(questionDTO.getQuestionNumber());
        Optional<Survey> surveyOptional = Optional.ofNullable(surveyRepository.findBySurveyId(questionDTO.getSurveyId()));
        if (surveyOptional.isPresent()) {
            question.setSurveyId(surveyOptional.get());
        } else {
            // 필요한 경우 적절한 예외를 던지거나 로깅
            throw new IllegalArgumentException("Survey with ID " + questionDTO.getSurveyId() + " not found");
        }
        question.setQuestion(questionDTO.getQuestion());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setOptionsType(questionDTO.getOptionsType());
        return question;
    }

    // Convert Entity to DTo
    public QuestionDTO convertQuestion(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionId(question.getQuestionId());
        questionDTO.setQuestionNumber(question.getQuestionNumber());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setQuestionType(question.getQuestionType());
        questionDTO.setSurveyId(question.getSurveyId().getSurveyId());
        questionDTO.setOptionsType(question.getOptionsType());
        return questionDTO;
    }
    // Create
    @Transactional
    public QuestionDTO save(QuestionDTO questionDTO) {
        Question question = convertDTO(questionDTO);
        Question saved = questionRepository.save(question);
        log.info("Question saved: {}", saved);

        return convertQuestion(saved);
    }

    // Read
    // Get ListBySurveyId
    public List<QuestionDTO> findBySurveyId(String surveyId) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        Survey survey = surveyRepository.findBySurveyId(surveyId);
        List<Question> questionList = questionRepository.findBySurveyIdOrderByQuestionNumberAsc(survey);
        for(Question question : questionList){
            QuestionDTO questionDTO = convertQuestion(question);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    // Get OneBySurveyIdAndQuestionNumber
    public QuestionDTO findBySurveyIdAndQuestionNumber(String surveyId, Integer questionNumber) {
        QuestionDTO questionDTO = new QuestionDTO();
        Survey survey = surveyRepository.findBySurveyId(surveyId);
        List<Question> questionList = questionRepository.findBySurveyIdOrderByQuestionNumberAsc(survey);
        for(Question question : questionList){
            if(question.getQuestionNumber().equals(questionNumber)){
                questionDTO = convertQuestion(question);
                break;
            }
        }

        return questionDTO;
    }

    // Get OneByQuestionId
    public QuestionDTO findByQuestionId(String questionId) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionRepository.findByQuestionId(questionId);
        questionDTO = convertQuestion(question);
        return questionDTO;
    }

    // Get One By QuestionId And Question Number
    public QuestionDTO findByQuestionIdAndQuestionNumber(String questionId, Integer nextQuestionNumber) {
        QuestionDTO questionDTO = questionRepository.findByQuestionIdAndQuestionNumber(questionId, nextQuestionNumber);
        return questionDTO;
    }

    // Modify
    @Transactional
    public QuestionDTO update(QuestionDTO questionDTO) {
        Question question = convertDTO(questionDTO);
        Question saved = questionRepository.save(question);
        log.info("Question Updated: {}", saved.getQuestionId());

        return convertQuestion(saved);
    }

    // Delete
    @Transactional
    public void delete(String questionId) {
        Question question = questionRepository.findByQuestionId(questionId);
        questionRepository.delete(question);
        log.info("Question Deleted: {}", question.getQuestionId());
    }



}
