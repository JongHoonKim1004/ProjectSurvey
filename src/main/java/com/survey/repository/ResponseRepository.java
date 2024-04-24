package com.survey.repository;

import com.survey.entity.Options;
import com.survey.entity.Question;
import com.survey.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, String> {
    public List<Response> findByQuestionId(Question question);

    public Response findByQuestionIdAndOptionsId(Question question, Options options);

    public Response findByResponseId(String responseId);
}
