package com.survey.repository;

import com.survey.entity.Options;
import com.survey.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionsRepository extends JpaRepository<Options, String> {
    public List<Options> findByQuestionIdOrderByOptionsNumberAsc(Question question);

    public Options findByOptionsId(String optionsId);
}
