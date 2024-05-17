package com.survey.controller;

import com.survey.dto.NoticeDTO;
import com.survey.entity.Notice;
import com.survey.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice/*")
@Slf4j
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    // Create
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody NoticeDTO noticeDTO) {
        log.info("Creating notice : {}", noticeDTO);
        NoticeDTO noticeDTO1 = noticeService.save(noticeDTO);
        log.info("Notice Saved: {}", noticeDTO1);

        return ResponseEntity.ok("Success");
    }

    // Read
    // GetList
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> list() {
        log.info("Getting Notice List...");
        return ResponseEntity.ok(noticeService.findAll());
    }

    // GetList with Paging
    @GetMapping("/list/page/{page}")
    public ResponseEntity<Page<NoticeDTO>> listPage(@PathVariable int page) {
        log.info("Getting Notice List... with Paging, {}", page);
        return ResponseEntity.ok(noticeService.noticePage(page));
    }

    // GetList with Paging for MainPage
    @GetMapping("/list/formain")
    public ResponseEntity<Page<NoticeDTO>> listForMain() {
        log.info("Getting Notice List... for Main Page");
        return ResponseEntity.ok(noticeService.noticePageforMainPage());
    }


    // GetOne
    @GetMapping("/read/{id}")
    public ResponseEntity<NoticeDTO> getById(@PathVariable Long id) {
        log.info("Getting Notice by id: {}", id);
        return ResponseEntity.ok(noticeService.findById(id));
    }

    // Update
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        noticeService.save(noticeDTO);
        log.info("Notice Updated: {}", noticeDTO);
        return ResponseEntity.ok("Success");
    }

    // Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info("Deleting Notice by id: {}", id);
        noticeService.delete(id);
        return ResponseEntity.ok("Success");
    }


}
