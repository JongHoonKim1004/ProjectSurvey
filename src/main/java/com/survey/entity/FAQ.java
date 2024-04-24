package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long faqId; // FAQ 식별자

    @Column(nullable = false)
    private String title; // 질문
    @Column(nullable = false)
    private String content; // 응답
}
