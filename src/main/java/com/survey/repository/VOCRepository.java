package com.survey.repository;

import com.survey.entity.Users;
import com.survey.entity.VOC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VOCRepository extends JpaRepository<VOC, String> {
    public VOC findByVocId(String vocId);

    public    List<VOC> findByWriter_Name(String Name);
}
