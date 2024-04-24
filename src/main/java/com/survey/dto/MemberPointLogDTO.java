package com.survey.dto;

import com.survey.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String logId; // 포인트 이력 식별자

    private String memberId; // 사업자 식별자

    private Integer PointChange; // 포인트 변동량

    private String ChangeType; // 변동 사유
    private LocalDateTime ChangeDate; // 변동 시간
}
