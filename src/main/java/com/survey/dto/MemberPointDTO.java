package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPointDTO {
    private String PointId; // 테이블 식별자

    private String MemberId; // 사업자 식별자

    private Integer PointTotal; // 총 충전 포인트

    private Integer PointUsed; // 사용 포인트

    private Integer PointBalance; // 포인트 잔액
}
