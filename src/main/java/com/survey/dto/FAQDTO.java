package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQDTO {
    private Long faqId; // FAQ 식별자

    private String title; // 질문
    private String content; // 응답
}
