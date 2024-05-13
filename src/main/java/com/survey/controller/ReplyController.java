package com.survey.controller;

import com.survey.dto.ReplyDTO;
import com.survey.dto.UsersDTO;
import com.survey.dto.VOC_DTO;
import com.survey.entity.Reply;
import com.survey.service.EmailService;
import com.survey.service.ReplyService;
import com.survey.service.UsersService;
import com.survey.service.VocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply/*")
@Slf4j
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private VocService vocService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsersService usersService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ReplyDTO replyDTO) {
        ReplyDTO replyDTO1 =  replyService.save(replyDTO);
        log.info("Reply created: {}", replyDTO1.toString());

        // 답글이 작성되면 이메일 전송
            // 1. 작성자 확인
        VOC_DTO vocDto = vocService.findByVocId(replyDTO1.getVocId());
        String usersName = vocDto.getWriter();
        UsersDTO usersDTO = usersService.findByName(usersName);
        log.info("이용자 {} 님에게 답글 작성 이메일을 전송합니다", usersName);

            // 2. 이메일 전송
        Integer code = emailService.createRandom();
        String content = emailService.createEmailWithCode("reply", code);
        try{
            emailService.sendEmail(usersDTO.getName(), content, "reply");
            log.info("Email Sent to : {}", usersDTO.getName());
        } catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok("Reply created");
    }

    // Read List 부분은 VocController 에서 List 를 받아오는 방법 채택


    // Update
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setId(id);
        replyService.save(replyDTO);
        log.info("Reply updated: {}", replyDTO.toString());
        return ResponseEntity.ok("Reply updated");
    }

    // Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        replyService.delete(id);
        log.info("Reply deleted: {}", id);
        return ResponseEntity.ok("Reply deleted");
    }


}
