package com.survey.service;

import com.survey.dto.ReplyDTO;
import com.survey.entity.Reply;
import com.survey.entity.VOC;
import com.survey.repository.ReplyRepository;
import com.survey.repository.VOCRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private VOCRepository vocRepository;

    // Convert DTO to Entity
    public Reply convertDTO(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        if(replyDTO.getId() != null) {
            reply.setId(replyDTO.getId());
        }
        reply.setVocId(vocRepository.findByVocId(replyDTO.getVocId()));
        reply.setReply(replyDTO.getReply());
        if(replyDTO.getRegDate() != null){
            reply.setRegDate(replyDTO.getRegDate());
        } else reply.setRegDate(LocalDateTime.now());
        return reply;
    }

    // Convert Entity to DTO
    public ReplyDTO convertEntity(Reply reply) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(reply.getId());
        replyDTO.setReply(reply.getReply());
        replyDTO.setRegDate(reply.getRegDate());
        replyDTO.setVocId(reply.getVocId().getVocId());
        return replyDTO;
    }

    // Create
    @Transactional
    public ReplyDTO save(ReplyDTO replyDTO) {
        Reply reply = convertDTO(replyDTO);
        Reply savedReply = replyRepository.save(reply);
        log.info("REPLY SAVED: {}", savedReply.getReply());

        return convertEntity(savedReply);
    }

    // Read
    public List<ReplyDTO> findByVocId(String vocId) {
        VOC voc = vocRepository.findByVocId(vocId);
        List<Reply> replyList = replyRepository.findByVocId(voc);
        List<ReplyDTO> replyDTOList = new ArrayList<>();
        for(Reply reply : replyList){
            ReplyDTO replyDTO = convertEntity(reply);
            replyDTOList.add(replyDTO);
        }
        return replyDTOList;
    }

    // Update
    @Transactional
    public void update(ReplyDTO replyDTO) {
        Reply reply = convertDTO(replyDTO);
        Reply savedReply = replyRepository.save(reply);
        log.info("REPLY UPDATED: {}", savedReply.getReply());
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        replyRepository.deleteById(id);
        log.info("REPLY DELETED: {}", id);
    }

    // Send Email when reply written
    // API 도입 후 작성
}
