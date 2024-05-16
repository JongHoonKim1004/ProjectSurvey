package com.survey.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersSurveyDTO {
    private String logId; // 참가 로그 식별자

    private String surveyId; // 설문조사 식별자

    private String usersId; // 이용자 식별자

    private String memberId; // 사업자 페이지용 사업자 식별자

    private LocalDateTime surveyDate; // 설문조사 응시일
    private Integer PointGiven; // 수령한 포인트
}
