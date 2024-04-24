package com.survey.repository;

import com.survey.entity.Question;
import com.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
    public List<Question> findBySurveyId(Survey survey);

    public Question findByQuestionId(String questionId);
}
