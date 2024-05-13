package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    private Long id; // 공지사항 식별자

    private String title; // 공지사항 제목
    private String content; // 내용

    private String writer; // 작성자(관리자 한정)

    private LocalDateTime regDate; // 작성일
    private LocalDateTime updateDate; // 수정일
    private Integer readCount = 0; // 조회수
}
