package ru.afanasyev.email.app;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class EmailService {
    private final String FILE_JSON_PATH = "/static/file.json";

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("classpath:/static/heart.gif")
    Resource resourceFile;
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    public EmailService(JavaMailSender emailSender, @Qualifier("emailSpringTemplateEngine") SpringTemplateEngine thymeleafTemplateEngine) {
        this.emailSender = emailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        mailMessage(message, to, subject, text);
        emailSender.send(message);
    }

    public void sendMimeMessage(String to, String subject, String text) {
        log.info("Sending mime message: {}", text);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            File file = new ClassPathResource(FILE_JSON_PATH).getFile();
            helper.addAttachment("File (Hello-World).json", file);
            emailSender.send(message);
            log.info("Message: {} successfully sent", text);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessageUsingThymeleafTemplate(String to, String subject, Template template) {
        log.info("Sending html message to {}", to);
        Context thymeleafContext = new Context();
        Map<String, Object> templateModel = Map.of(
            "recipientName", to,
            "senderName", emailFrom
        );
        thymeleafContext.setVariables(templateModel);
        String htmlBody;
        if (Template.SIMPLE.equals(template)) {
            htmlBody = thymeleafTemplateEngine.process("template-simple.html", thymeleafContext);
        } else {
            htmlBody = thymeleafTemplateEngine.process("template-with-love.html", thymeleafContext);
        }

        try {
            sendHtmlMessage(to, subject, htmlBody, template);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private MailMessage mailMessage(MailMessage message, String to, String subject, String text) {
        message.setTo(to);
        message.setFrom(emailFrom);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody, Template template) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        if (Template.LOVE.equals(template)) {
            helper.addInline("heart.gif", resourceFile);
        }
        emailSender.send(message);
    }
}
