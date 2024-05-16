package com.survey.dto;

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
public class VOC_DTO {
    private String VocId; // 일대일 문의 식별자

    private String title; // 문의 제목

    private String content; // 문의 내용

    private String writer; // 작성자(이용자)

    private LocalDateTime regDate; // 작성일
    private Boolean reply = false; // 답변 여부

    private Survey SurveyId; // 설문조사 식별자(선택)

}
