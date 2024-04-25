package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPointDTO {
    private String pointId; // 테이블 식별자

    private String usersId; // 이용자 식별자

    private Integer pointTotal;  // 총 적립 포인트

    private Integer pointUsed; // 사용 포인트

    private Integer pointBalance; // 포인트 잔액

}
