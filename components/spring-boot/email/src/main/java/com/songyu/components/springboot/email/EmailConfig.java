package com.songyu.components.springboot.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.SSLSocketFactory;
import java.util.Properties;

/**
 * <p>
 * 邮件启动配置类
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:59
 */
@Configuration
@PropertySource("classpath:songyu-email.properties")
public class EmailConfig {

    /**
     * 邮件发送邮箱
     */
    public static final String EmailSender = "songyuwong@163.com";

    @Value("${songyu.email.username}")
    private String username;

    @Value("${songyu.email.password}")
    private String password;

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.163.com");
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.socketFactory.port", 465);
        mailProperties.put("mail.smtp.socketFactory.class", SSLSocketFactory.class);
        javaMailSender.setJavaMailProperties(mailProperties);
        return javaMailSender;
    }

    @Bean
    public EmailSendService emailSendService(JavaMailSender javaMailSender) {
        return new EmailSendService(javaMailSender);
    }

}
