package com.karol172.blog.configuration;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig
{
    @Bean(name={"passwordEncoder"})
    public PasswordEncoder getBCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean(name={"mailSender"})
    public JavaMailSender getJavaMailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.wp.pl");
        mailSender.setPort(465);
        mailSender.setUsername("brzeczyszczykiewiczgrz@wp.pl");
        mailSender.setPassword("projekt123456");
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtps.ssl.checkserveridentity", "true");

        return mailSender;
    }
}
