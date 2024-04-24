package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logId; // 포인트 이력 식별자

    @ManyToOne
    @JoinColumn(name = "UsersId")
    private Users usersId; // 이용자 식별자

    @Column(nullable = false)
    private Integer pointChange; // 포인트 변동량

    @Column(nullable = false)
    private String changeType; // 변동 사유
    private LocalDateTime changeDate; // 변동 시간
}
