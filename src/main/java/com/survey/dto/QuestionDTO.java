package com.survey.dto;

import com.survey.entity.Survey;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private String questionId; // 질문 식별자

    private Integer questionNumber; // 질문 번호

    private String surveyId; // 설문조사 식별자

    private String question; // 질문 내용

    private String questionType; // 질문 유형(input type 기재)

    private String optionsType; // 응답 유형(1개 선택, 출생연도 기입 등등 기재)

}
