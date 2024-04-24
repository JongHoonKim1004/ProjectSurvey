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
public class Response {
    @Id
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String responseId; // 응답 식별자

    @ManyToOne
    @JoinColumn(name = "QuestionId")
    private Question questionId; // 질문 식별자

    @ManyToOne
    @JoinColumn(name = "OptionsId")
    private Options optionsId; // 선택지 식별자

    @ManyToOne
    @JoinColumn(name = "UsersId")
    private Users usersId; // 이용자 식별자
    private String responseText; // 응답이 텍스트일 경우 응답
}
