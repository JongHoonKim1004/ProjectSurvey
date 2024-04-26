package com.survey.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService  {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    // 인증번호 생성 로직
    public int createRandom(){
        Random rand = new Random();
        return rand.nextInt(888888) + 111111;
    }

    // 회원가입 or 비밀번호 찾기 에 따른 인증번호 메일 내용 설정
    public String createEmailWithCode(String when, Integer code){

        log.info("인증번호 생성이 완료되었습니다. : {}", code);
        String str = "<html><body><div style='text-align: center;'><h1>안녕하세요, 엠브레인 패널파워 입니다</h1>";
        if(when.contentEquals("register")){
            str += "<br><p>회원가입 인증을 위한 인증번호를 입력해주세요</p><br>";
            str += code;
            str += "<br><p>감사합니다.</p><br>";
        } else if(when.contentEquals("passwordFind")){
            str += "<br><p>비밀번호 찾기 인증을 위한 인증번호를 입력해주세요</p><br>";
            str += code;
            str += "<br><p>감사합니다.</p><br>";
        } else if(when.contentEquals("memberPoint")){
            str += "<br><p>회원님의 잔여 포인트가 얼마 남지 않았습니다.</p><br>";
            str += "<br><p>포인트 잔액이 부족할 경우 진행중인 설문조사가 종료될 수 있습니다.</p><br>";
        } else if(when.contentEquals("reply")){
            str += "<br><p>회원님이 작성해 주신 1:1 문의의 답변이 작성되었습니다</p><br>";
            str += "<br><p>확인해 주시면 감사하겠습니다</p><br>";
        }

        str += "</div></body></html>";
        return str;
    }

    // 실제 이메일 전송
    public void sendEmail(String to, String content, String when) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        if(when.contentEquals("passwordFind")){
            mimeMessageHelper.setSubject("비밀번호 찾기 인증번호입니다");
        } else if(when.contentEquals("memberPoint")){
            mimeMessageHelper.setSubject("포인트가 부족해지고 있습니다");
        } else if(when.contentEquals("register")){
            mimeMessageHelper.setSubject("회원가입 인증번호입니다.");
        }
        mimeMessageHelper.setText(content, true);

        mailSender.send(mimeMessage);
        log.info("이메일 전송이 완료되었습니다.");
    }
    
}
