package com.survey.controller;

import com.survey.dto.AdminDTO;
import com.survey.dto.MemberDTO;
import com.survey.dto.UsersDTO;
import com.survey.service.AdminService;
import com.survey.service.MemberService;
import com.survey.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserCheckController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/userCheck/{username}")
    public ResponseEntity<Boolean> userCheck(@PathVariable String username) {
        AdminDTO admin = adminService.findByName(username);
        log.info("Admin Name Check Complete");
        MemberDTO member = memberService.findByName(username);
        log.info("Member Name Check Complete");
        UsersDTO user = usersService.findByName(username);
        log.info("User Name Check Complete");

        log.info("All Name Check Complete");
        // 하나라도 아이디 정보가 있으면 false, 겹치는게 없으면 true
        return admin == null && member == null && user == null ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }
}
