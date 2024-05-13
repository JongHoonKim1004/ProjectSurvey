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
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 공지사항 식별자

    @Column(nullable = false)
    private String title; // 공지사항 제목
    @Column(nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private String writer; // 작성자(관리자 한정)

    private LocalDateTime regDate; // 작성일
    private LocalDateTime updateDate; // 수정일
    private Integer readCount; // 조회수
}
