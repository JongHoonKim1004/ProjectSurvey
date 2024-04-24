package com.survey.repository;

import com.survey.entity.Reply;
import com.survey.entity.VOC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {


    List<Reply> findByVocId(VOC voc);
}
