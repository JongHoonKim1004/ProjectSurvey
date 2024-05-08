package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPointLogDTO {
    private long logId; // 포인트 이력 식별자

    private String memberId; // 사업자 식별자

    private Integer pointChange; // 포인트 변동량

    private String changeType; // 변동 사유
    private LocalDateTime changeDate; // 변동 시간
}
