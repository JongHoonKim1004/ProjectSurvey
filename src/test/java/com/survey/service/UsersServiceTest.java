package com.survey.service;

import com.survey.dto.UsersDTO;
import com.survey.entity.Users;
import com.survey.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void registerUser() {
        // 기본 데이터 생성
        UsersDTO dto = UsersDTO.builder().
                name("zima0412@gmail.com").
                nickname("zima").
                phone("01050956331").
                addr("주소").
                zipNo("16380").
                birth("19941004").
                gender("male").
                occupation("student").
                married("none").build();

        String password = "123456";
        // 예상 데이터 생성
        Users expectedUser = Users.builder().
                name("zima0412@gmail.com").
                nickname("zima").
                phone("01050956331").
                addr("주소").
                zipNo("16380").
                birth("19941004").
                gender("male").
                occupation("student").
                married("none").build();


        // 실제 데이터 생성
        Users result = usersService.registerUser(dto, password);

        // 비교
        assertEquals(expectedUser, result);
    }
}