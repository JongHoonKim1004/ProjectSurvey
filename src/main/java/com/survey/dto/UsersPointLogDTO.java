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
public class UsersPointLogDTO {
    private Long logId; // 포인트 이력 식별자

    private String UsersId; // 이용자 식별자

    private Integer PointChange; // 포인트 변동량

    private String ChangeType; // 변동 사유
    private LocalDateTime ChangeDate; // 변동 시간
}
