package com.songyu.components.springboot.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
public class EmailConfig {

    /**
     * 邮件发送邮箱
     */
    public static final String EmailSender = "songyuwong@163.com";

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.163.com");
        javaMailSender.setUsername("ENC(0ovH7QJ5DNAhgNHj5UZl2eGKws/EKb3tmj5nY4sLAF0=)");
        javaMailSender.setPassword("ENC(TeRAtfp/ccI+hQ37SBX31pnGX3pb10B5l0nkqCC2oxU=)");
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
