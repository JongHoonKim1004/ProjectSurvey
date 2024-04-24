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
public class MemberSurveyDTO {
    private String logId; // 생성 로그 식별자

    private String surveyId; // 설문조사 식별자

    private String memberId; // 사업자 식별자

    private LocalDateTime regDate = LocalDateTime.now(); // 설문조사 등록일
    private LocalDateTime startDate; // 설문조사 시작일
    private LocalDateTime endDate; // 설문조사 마감일
    private Integer PointGive; // 지급할 포인트
    private Integer PointAtLeastGive; // 지급할 최소 포인트
}
