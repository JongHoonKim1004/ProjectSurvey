package com.survey.config;

import com.survey.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 스프링 시큐리티 6.1 부터 체인 방법으로 작성, 리턴에서 내용 작성
        return http
                // 접속 할 사이트 제한
                .authorizeHttpRequests(authorizeRequests ->{
                        authorizeRequests.requestMatchers("/**").permitAll();

                        authorizeRequests.anyRequest().authenticated();
                        // 특정 사이트는 누구나 접속 가능, 그 외의 사이트는 인증된 사람만 접속 가능
                })
                // cors 기본 설정
                .cors(Customizer.withDefaults())

                // session 기반이 아님을 선언
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // httpBasic 비활성화
                .httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.disable();
                })

                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 소셜로그인 설정


        // filter 등록
        .addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
