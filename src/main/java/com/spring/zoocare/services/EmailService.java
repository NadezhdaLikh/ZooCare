package com.spring.zoocare.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendPasswordEmail(String recipientEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Ваш пароль");
        message.setText("Здравствуйте! Это ваш новый пароль: " + password + ".\nПозднее вы сможете поменять его, если захотите.");
        javaMailSender.send(message);
    }
}
