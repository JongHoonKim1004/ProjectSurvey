package com.survey.controller;

import com.survey.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email/*")
@Slf4j
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/send/{when}")
    public Integer sendEmail(@RequestParam("to") String to, @PathVariable String when){
        Integer code = emailService.createRandom();

        try{

            String content = emailService.createEmailWithCode(when, code);
            emailService.sendEmail(to, content, when);
            log.info("Email sent successfully, Code: {}", code);
            return code;
        } catch(Exception e){
            e.printStackTrace();
        }

        return code;
    }
}
