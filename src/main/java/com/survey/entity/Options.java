package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Options {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String optionsId; // 선택지 식별자

    @ManyToOne
    @JoinColumn(name = "QuestionId")
    private Question questionId; // 질문 식별자
    
    private Integer optionsNumber; // 선택지 번호

    @Column(nullable = false)
    private String options; // 질문 내용

    @Column(columnDefinition = "boolean default false")
    private boolean terminate; // 조기종료 여부
}
