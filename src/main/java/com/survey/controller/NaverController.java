package com.survey.controller;

import com.survey.dto.NaverDTO;
import com.survey.dto.UsersDTO;
import com.survey.entity.Users;
import com.survey.security.TokenProvider;
import com.survey.service.NaverService;
import com.survey.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class NaverController {
    @Autowired
    private NaverService naverService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/login/oauth/naver")
    public void naverLogin(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response, Authentication authentication) throws IOException {
        // access_token
        String access_token = naverService.getAccessToken(code, state);

        // userInfo
        NaverDTO naverDTO = naverService.getUserInfo(access_token);

        String name = naverDTO.getEmail();

        UsersDTO usersDTO = usersService.findByName(name);

        String token = tokenProvider.create(usersService.convertDTO(usersDTO));

        response.sendRedirect("http://localhost:3000/login/oauth/naver?accessToken=" + access_token + "&token=" + token);
    }
}
