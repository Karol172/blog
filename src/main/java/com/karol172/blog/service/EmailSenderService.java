package com.karol172.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail (String to, String title, Context content) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(templateEngine.process("mail", content), true);
            helper.setFrom("brzeczyszczykiewiczgrz@wp.pl");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }
}
