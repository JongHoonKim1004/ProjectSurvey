package com.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 답변 식별자

    @ManyToOne
    @JoinColumn(name = "VocId")
    private VOC vocId; // 일대일 문의 식별자

    @Column(nullable = false)
    private String reply; // 답변 내용
    private LocalDateTime regDate; // 답변 작성일
}
