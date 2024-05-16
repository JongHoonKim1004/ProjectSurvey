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
public class UsersSurvey {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String logId; // 참가 로그 식별자

    @ManyToOne
    @JoinColumn(name = "SurveyId")
    private Survey surveyId; // 설문조사 식별자

    @ManyToOne
    @JoinColumn(name = "UsersId")
    private Users usersId; // 이용자 식별자

    private String memberId; // 사업자 페이지용 사업자 식별자
    private LocalDateTime surveyDate; // 설문조사 응시일
    private Integer pointGiven; // 수령한 포인트
}
