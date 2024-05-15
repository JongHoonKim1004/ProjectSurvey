package com.survey.controller;

import com.survey.dto.AdminDTO;
import com.survey.dto.ErrorDTO;
import com.survey.entity.Admin;
import com.survey.security.TokenProvider;
import com.survey.service.AdminService;
import io.jsonwebtoken.security.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/*")
@Slf4j
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody AdminDTO adminDTO) {
        String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
        adminDTO.setPassword(encodedPassword);
        AdminDTO adminDTO1 = adminService.save(adminDTO);
        log.info("Admin Saved Complete: {}", adminDTO1.toString());
        return ResponseEntity.ok("Admin Saved");
    }

    // Read
    // Get Admin List
    @GetMapping("/list")
    public ResponseEntity<List<AdminDTO>> getAll(){
        List<AdminDTO> adminDTOList = adminService.getAllAdmins();
        return ResponseEntity.ok(adminDTOList);
    }

    // Get Myself
    @GetMapping("/id/{adminId}")
    public ResponseEntity<AdminDTO> getMySelf(@PathVariable String adminId){
        AdminDTO adminDTO = adminService.getMyself(adminId);
        return ResponseEntity.ok(adminDTO);
    }

    // Get Myself By EmployeeNumber
    @GetMapping("/employeeNo/{employeeNo}")
    public ResponseEntity<AdminDTO> getMyselfByEmployeeNo(@PathVariable String employeeNo){
        AdminDTO adminDTO = adminService.findByEmployeeNo(employeeNo);
        return ResponseEntity.ok(adminDTO);
    }

    // Update
    @PostMapping("/update/{adminId}")
    public ResponseEntity<String> update(@PathVariable String adminId, @RequestBody AdminDTO adminDTO){
        adminDTO.setAdminId(adminId);
        adminService.update(adminDTO);
        log.info("Admin Updated Complete: {}", adminDTO.toString());
        return ResponseEntity.ok("Admin Updated");
    }

    // Delete
    @PostMapping("/delete/{adminId}")
    public ResponseEntity<String> delete(@PathVariable String adminId){
        adminService.delete(adminId);
        log.info("Admin Deleted Complete: {}", adminId);
        return ResponseEntity.ok("Admin Deleted");
    }

    // Login

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminDTO adminDTO){
        Admin admin = adminService.getByCredentials(adminDTO.getName(), adminDTO.getPassword(), passwordEncoder);
        log.info("Admin Found: {}", admin);

        if(admin != null){
            final String token = tokenProvider.create(admin);
            final AdminDTO adminDTO1 = AdminDTO.builder()
                    .name(admin.getName())
                    .nickname(admin.getNickname())
                    .token(token)
                    .build();

            return ResponseEntity.ok(adminDTO1);
        } else {
            ErrorDTO errorDTO = new ErrorDTO().builder().error("Invalid username or password").build();
            return ResponseEntity.badRequest().body(errorDTO);
        }
    }

    // 관리자 권한으로 이용자, 사업자에 사용할 수 있는 기능
}
