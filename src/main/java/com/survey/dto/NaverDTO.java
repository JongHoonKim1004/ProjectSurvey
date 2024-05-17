package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverDTO {
    private String name; // 회원이름
    private String email; // 연락처 이메일 주소
    private String access_token; // 접근 토큰
}
