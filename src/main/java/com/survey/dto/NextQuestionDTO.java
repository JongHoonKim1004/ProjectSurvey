package com.survey.dto;

import com.survey.entity.Options;
import com.survey.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NextQuestionDTO {
    // 설문 조사중에 다음 질문을 가져오는 DTO


    private QuestionDTO question;
    private List<OptionsDTO> options;
}
