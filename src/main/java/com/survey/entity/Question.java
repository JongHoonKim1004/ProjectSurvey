package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String questionId; // 질문 식별자


    private Integer questionNumber; // 질문 번호
    
    @ManyToOne
    @JoinColumn(name="SurveyId")
    private Survey surveyId; // 설문조사 식별자

    @Column(length = 100, nullable = false)
    private String question; // 질문 내용

    @Column(nullable = false)
    private String questionType; // 질문 유형(input type 을 기재)

    @Column(nullable = false)
    private String optionsType; // 응답 유형(1개 선택, 출생연도 기입 등등 기재)

}
