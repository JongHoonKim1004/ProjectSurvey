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
public class ReplyDTO {
    private Long id; // 답변 식별자

    private String vocId; // 일대일 문의 식별자

    private String reply; // 답변 내용
    private LocalDateTime regDate; // 답변 작성일
}
