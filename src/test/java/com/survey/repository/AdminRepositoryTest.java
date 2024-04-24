package com.survey.repository;

import com.survey.entity.Admin;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;

    // save
    @Test
    void save(){
        Admin admin = new Admin();
        admin.setName("admin");
        admin.setPassword("admin");
        admin.setNickname("admin");
        admin.setEmployeeNo("12345678");
        adminRepository.save(admin);

    }
    @Test
    void findByEmployeeNo() {
        Admin admin = Admin.builder().name("admin").nickname("admin").employeeNo("12345678").build();
        adminRepository.save(admin);
        Admin findAdmin = adminRepository.findByEmployeeNo("12345678");

        System.out.println(findAdmin.toString());
    }

    @Test
    void findByname() {
    }
}