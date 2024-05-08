package com.survey.controller;

import com.survey.dto.FAQDTO;
import com.survey.service.FAQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
@Slf4j
public class FaqController {
    @Autowired
    private FAQService faqService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody FAQDTO faqDTO) {
        FAQDTO faqdto = faqService.save(faqDTO);
        log.info("faq created: {}", faqdto);

        return ResponseEntity.ok("Success");
    }

    // Read
    // GetList
    @GetMapping("/list")
    public ResponseEntity<List<FAQDTO>> getList(){
        List<FAQDTO> faqDTOList = faqService.findAll();
        return ResponseEntity.ok(faqDTOList);
    }

    // GetOne
    @GetMapping("/read/{id}")
    public ResponseEntity<FAQDTO> getById(@PathVariable Long id){
        FAQDTO faqDTO = faqService.findById(id);
        return ResponseEntity.ok(faqDTO);
    }

    // Update
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody FAQDTO faqDTO){
        faqDTO.setFaqId(id);
        faqService.update(faqDTO);
        log.info("faq updated: {}", faqDTO);
        return ResponseEntity.ok("Success");
    }

    // Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        faqService.delete(id);
        log.info("faq deleted: {}", id);
        return ResponseEntity.ok("Success");
    }

}
