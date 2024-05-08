package com.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String usersId; // 이용자 식별자

    @Column(unique = true, nullable = false)
    private String name; // 이용자 아이디(이메일)

    @Column(nullable = false)
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
}
