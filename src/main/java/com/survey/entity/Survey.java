package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Survey {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String surveyId; // 설문조사 식별자

    @Column(nullable = false)
    private String name; // 설문조사 이름

    private String description; // 설문조사 설명


    private String memberId; // 설문 등록 사업자

    @Column(nullable = false)
    private Integer point; // 지급 포인트

    @Column(nullable = false)
    private Integer pointAtLeast; // 최소 지급 포인트

    private LocalDateTime regDate; // 설문 등록일
    private LocalDateTime startDate; // 설문 시작일
    private LocalDateTime endDate; // 설문 마감일

    private Integer surveyParticipate; // 설문조사 참여 인원
}
