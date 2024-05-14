package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDTO {
    private String usersId; // 이용자 식별자

    private String name; // 이용자 아이디(이메일)

    private String nickname; // 이용자 이름
    private String password; // 비밀번호
    private String phone; // 전화번호
    private String addr; // 주소
    private String addrDetail; // 상세주소
    private String zipNo; // 우편번호
    private String birth; // 생년월일
    private String gender; // 성별
    private String occupation; // 직업
    private String married; // 결혼 여부

    private String token; // JWT Token
}
