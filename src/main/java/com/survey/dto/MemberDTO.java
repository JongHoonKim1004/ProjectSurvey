package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private String MemberId; // 사업자 식별자

    private String name; // 사업자 아이디(이메일)

    private String nickname; // 사업자 상호명

    private String password; // 사업자 비밀번호

    private String addr; // 사업자 소재지

    private String addrDetail; // 소재지 상세주소

    private String zipNo; // 소재지 우편번호

    private String phone; // 사업자 연락처

    private LocalDate estDate; // 사업자 설립일

    private BigInteger compNo; // 사업자등록번호

    private String token; // JWT Token
}
