package com.survey.repository;

import com.survey.entity.Member;
import com.survey.entity.MemberSurvey;
import com.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSurveyRepository extends JpaRepository<MemberSurvey, String> {

    List<MemberSurvey> findByMemberId(Member byMemberId);

    public MemberSurvey findByLogId(String logId);

    List<MemberSurvey> findBySurveyId(Survey surveyId);
}
