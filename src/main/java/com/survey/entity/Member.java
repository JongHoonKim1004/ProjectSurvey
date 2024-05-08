package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigInteger;
import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String memberId; // 사업자 식별자

    @Column(unique = true, nullable = false)
    private String name; // 사업자 아이디(이메일)


    @Column(unique = true, nullable = false)
    private String nickname; // 사업자 상호명

    private String password; // 사업자 비밀번호

    private String zipNo; // 소재지 우편번호
    
    private String addr; // 사업자 소재지
    
    private String addrDetail; // 소재지 상세주소

    private String phone; // 사업자 연락처


    private LocalDate estDate; // 사업자 설립일

    @Column(unique = true, nullable = false)
    private BigInteger compNo; // 사업자등록번호
            
}
