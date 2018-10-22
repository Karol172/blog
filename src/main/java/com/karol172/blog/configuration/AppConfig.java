package com.karol172.blog.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

@Configuration
public class AppConfig {

    @Bean(name="passwordEncoder")
    public PasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name="mailSender")
    public JavaMailSender getJavaMailSender() {
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

    @Bean(name="configuration")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AppConfiguration getAppConfiguration () {
        try {
            File fXmlFile = new File(getClass().getResource("/static/xml/configuration.xml").getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("configuration");
            Node node = nList.getLength() == 1 ? nList.item(0) : null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                AppConfiguration appConfiguration = new AppConfiguration();
                appConfiguration.setArticlesPerPage(Integer.valueOf(element.getElementsByTagName("articlesPerPage").item(0).getTextContent()));
                appConfiguration.setTitlePage(element.getElementsByTagName("titlePage").item(0).getTextContent());
                return appConfiguration;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AppConfiguration(10, "Blog");
    }
}
