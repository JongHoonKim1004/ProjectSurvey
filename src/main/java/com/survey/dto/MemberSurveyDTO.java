package com.survey.dto;

import com.survey.entity.Member;
import com.survey.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSurveyDTO {
    private String logId; // 생성 로그 식별자

    private String surveyId; // 설문조사 식별자

    private String memberId; // 사업자 식별자

    private String usersId; // 설문 응사자 식별자

    private Integer pointGiven; // 지급한 포인트
    private LocalDateTime surveyTime; // 참여 시간
}
