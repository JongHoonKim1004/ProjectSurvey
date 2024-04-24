package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSurvey {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String logId; // 생성 로그 식별자

    @OneToOne
    @JoinColumn(name = "SurveyId")
    private Survey surveyId; // 설문조사 식별자

    @ManyToOne
    @JoinColumn(name = "MemberId")
    private Member memberId; // 사업자 식별자

    private LocalDateTime regDate = LocalDateTime.now(); // 설문조사 등록일
    private LocalDateTime startDate; // 설문조사 시작일
    private LocalDateTime endDate; // 설문조사 마감일
    private Integer pointGive; // 지급할 포인트
    private Integer pointAtLeastGive; // 지급할 최소 포인트
    

}
