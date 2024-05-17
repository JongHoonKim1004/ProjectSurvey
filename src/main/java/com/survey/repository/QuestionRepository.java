package com.survey.repository;

import com.survey.dto.QuestionDTO;
import com.survey.entity.Question;
import com.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
    public List<Question> findBySurveyIdOrderByQuestionNumberAsc(Survey survey);

    public Question findByQuestionId(String questionId);

    public QuestionDTO findByQuestionIdAndQuestionNumber(String questionId, Integer nextQuestionNumber);
}
