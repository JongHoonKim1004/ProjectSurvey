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
    // 만약 불러오는 질문에 이미 응답을 했다면 응답도 같이 불러온다

    private QuestionDTO question;
    private List<OptionsDTO> options;
    private List<ResponseDTO> response;
}
