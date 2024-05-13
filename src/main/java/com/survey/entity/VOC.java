package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Voice Of Customer
public class VOC {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String vocId; // 일대일 문의 식별자

    @Column(nullable = false)
    private String title; // 문의 제목


    private String content; // 문의 내용

    @ManyToOne
    @JoinColumn(name="writer", referencedColumnName = "name")
    private Users writer; // 작성자(이용자)

    private LocalDateTime regDate; // 작성일
    private Boolean reply; // 답변 여부
    @ManyToOne
    @JoinColumn(name = "SurveyId", referencedColumnName = "SurveyId", nullable = true)
    private Survey surveyId; // 설문조사 식별자(선택)

}
