package com.survey.controller;

import com.survey.dto.VOC_DTO;
import com.survey.dto.VocReplyDTO;
import com.survey.service.UsersService;
import com.survey.service.VocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voc")
@Slf4j
public class VocController {
    @Autowired
    private VocService vocService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(VOC_DTO dto){
        vocService.save(dto);
        log.info("VOC Saved: {}", dto.toString());

        return ResponseEntity.ok("VOC Saved");
    }

    // Read
    // GetList for Customer
    @GetMapping("/list/{writer}")
    public ResponseEntity<List<VOC_DTO>> list(@PathVariable String writer){
        List<VOC_DTO> vocList = vocService.findByWriter(writer);
        return ResponseEntity.ok(vocList);
    }

    // GetList for Admin
    @GetMapping("/list/all")
    public ResponseEntity<List<VOC_DTO>> listAll(){
        List<VOC_DTO> vocList = vocService.findAll();
        return ResponseEntity.ok(vocList);
    }

    // GetOne
    @GetMapping("/one/{vocId}")
    public ResponseEntity<VocReplyDTO> getOne(@PathVariable String vocId){
        VocReplyDTO vocReplyDTO = vocService.getVocAndReplyList(vocId);
        return ResponseEntity.ok(vocReplyDTO);
    }

    // Update
    @PostMapping("/update/{vocId}")
    public ResponseEntity<String> update(@PathVariable String vocId, VOC_DTO dto){
        vocService.update(dto);
        log.info("VOC Updated: {}", dto.toString());
        return ResponseEntity.ok("VOC Updated");
    }


    // Delete
    @PostMapping("/delete/{vocId}")
    public ResponseEntity<String> delete(@PathVariable String vocId){
        vocService.delete(vocId);
        log.info("VOC Deleted: {}", vocId);
        return ResponseEntity.ok("VOC Deleted");
    }
}
