package com.survey.dto;

import java.time.LocalDateTime;

public class SurveyDTO {
    private String SurveyId; // 설문조사 식별자

    private String name; // 설문조사 이름

    private String description; // 설문조사 설명

    private String surveyMember; // 설문 등록 사업자

    private Integer point; // 지급 포인트

    private Integer pointAtLeast; // 최소 지급 포인트

    private LocalDateTime regDate; // 설문 등록일
    private LocalDateTime endDate; // 설문 마감일

    private Integer SurveyParticipate; // 설문조사 참여 인원
}
