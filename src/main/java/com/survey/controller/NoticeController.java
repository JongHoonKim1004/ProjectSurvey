package com.survey.controller;

import com.survey.dto.NoticeDTO;
import com.survey.entity.Notice;
import com.survey.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice/*")
@Slf4j
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody NoticeDTO noticeDTO) {
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
