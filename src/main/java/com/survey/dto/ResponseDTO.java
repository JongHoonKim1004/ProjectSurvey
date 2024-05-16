package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {
    private String responseId; // 응답 식별자

    private String questionId; // 질문 식별자

    private List<String> optionsId; // 선택지 식별자

    private String usersId; // 이용자 식별자
    private String responseText; // 응답이 텍스트일 경우 응답
}
