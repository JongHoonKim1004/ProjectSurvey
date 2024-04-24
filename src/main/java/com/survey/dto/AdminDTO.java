package com.survey.dto;

import jakarta.persistence.Column;
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

    private String nickname; // 이름

    private String phone; // 전화번호

    private String employeeNo; // 사원번호
}
