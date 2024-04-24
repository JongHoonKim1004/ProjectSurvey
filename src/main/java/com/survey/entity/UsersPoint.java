package com.survey.entity;

import jakarta.persistence.*;
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
public class UsersPoint {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String pointId; // 테이블 식별자

    @ManyToOne
    @JoinColumn(name = "UsersId")
    private Users usersId; // 이용자 식별자

    @Column(nullable = false)
    private Integer pointTotal;  // 총 적립 포인트

    @Column(nullable = false)
    private Integer pointUsed; // 사용 포인트

    private Integer pointBalance; // 포인트 잔액
}
