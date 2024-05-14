package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {
    private String AdminId; // 식별자

    private String name; // 아이디(이메일)

    private String password; // 비밀번호

    private String nickname; // 이름

    private String phone; // 전화번호

    private String employeeNo; // 사원번호

    private String token; // JWT Token
}
