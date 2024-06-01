package com.systemedebons.bonification.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);

    }

    public void sendWelcomeEmail(String to) {
        String subject = "Bienvenue chez nous!";
        String text = "Merci de vous être inscrit!";
        sendEmail(to, subject, text);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Réinitialisation de votre mot de passe";
        String text = "Cliquez sur le lien pour réinitialiser votre mot de passe : " + "http://localhost:8080/reset-password?token=" + token;
        sendEmail(to, subject, text);
    }

}
