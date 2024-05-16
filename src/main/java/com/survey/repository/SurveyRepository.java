package com.survey.repository;

import com.survey.entity.Member;
import com.survey.entity.Survey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, String> {
    public Survey findBySurveyId(String surveyId);

    public List<Survey> findByMemberId(String memberId);

    public List<Survey> findByEndDateAfter(LocalDateTime now, Pageable pageable);
}
