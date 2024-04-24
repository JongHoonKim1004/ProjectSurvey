package com.survey.dto;

import com.survey.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionsDTO {
    private String optionsId; // 선택지 식별자

    private String questionId; // 질문 식별자

    private Integer optionsNumber; // 선택지 번호

    private String options; // 질문 내용

    private boolean terminate; // 조기종료 여부
}
