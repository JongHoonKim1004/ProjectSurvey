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

    @ManyToOne
    @JoinColumn(name = "SurveyId", unique = false)
    private Survey surveyId; // 설문조사 식별자

    @ManyToOne
    @JoinColumn(name = "MemberId")
    private Member memberId; // 사업자 식별자

    private String usersId; // 설문 응사자 식별자

    private Integer pointGiven; // 지급한 포인트
    private LocalDateTime surveyTime; // 참여 시간
    

}
