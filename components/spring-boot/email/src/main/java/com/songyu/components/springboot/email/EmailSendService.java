package com.songyu.components.springboot.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * <p>
 * 邮件发送相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 12:03
 */
@Slf4j
public class EmailSendService {

    private final JavaMailSender mailSender;

    public EmailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendText(String from, String to, String subject, String simpleText) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(simpleText);
        mailSender.send(simpleMailMessage);
    }

    public void sendHtml(String from, String to, String subject, String content) {
        try {
            //注意这里使用的是MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                //第二个参数：格式是否为html
                helper.setText(content, true);
            } catch (MessagingException e) {
                throw new RuntimeException(String.format("邮件格式错误 %s", e.getMessage()), e);
            }
            mailSender.send(message);
        } catch (MailSendException e) {
            throw new RuntimeException(parseEmailException(e, from, to), e);
        }
    }

    public void sendAttached(String from, String to, String subject, String content, String filePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            //要带附件第二个参数设为true
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    private String parseEmailException(Throwable t, String from, String to) {
        if (t.getMessage().contains("Invalid Addresses")) {
            return "非法的邮件地址 >> ".concat(to);
        } else {
            return t.getMessage();
        }
    }

}
