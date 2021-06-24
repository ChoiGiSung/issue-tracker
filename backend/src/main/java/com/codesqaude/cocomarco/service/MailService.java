package com.codesqaude.cocomarco.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    public JavaMailSender javaMailSender;

    @Async
    public void sendMail(String email, int code) {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setTo(email);
        simpleMessage.setSubject("이메일 인증");
        simpleMessage.setText("인증번호:" + code);
        javaMailSender.send(simpleMessage);
    }

    public int getRandom() {
        return (int) ((Math.random() * (1_000_000 - 100_000)) + 100_000);
    }
}
