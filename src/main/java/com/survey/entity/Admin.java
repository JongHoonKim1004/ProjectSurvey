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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Admin {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String adminId; // 식별자

    @Column(unique = true, nullable = false)
    private String name; // 아이디(이메일)

    private String password; // 비밀번호

    @Column(nullable = false)
    private String nickname; // 이름

    private String phone; // 전화번호

    @Column(nullable = false)
    private String employeeNo; // 사원번호
}
