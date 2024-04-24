package com.survey.repository;

import com.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, String> {
    public Survey findBySurveyId(String surveyId);

    public List<Survey> findBySurveyMember(String memberId);
}
