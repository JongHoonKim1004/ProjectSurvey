package com.survey.controller;

import com.survey.dto.ReplyDTO;
import com.survey.entity.Reply;
import com.survey.service.ReplyService;
import com.survey.service.VocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ReplyDTO replyDTO) {
        replyService.save(replyDTO);
        log.info("Reply created: {}", replyDTO.toString());
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
