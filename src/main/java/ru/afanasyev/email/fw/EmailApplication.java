package ru.afanasyev.email.fw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.afanasyev.email.app.EmailService;
import ru.afanasyev.email.app.Template;

@SpringBootApplication(scanBasePackages = "ru.afanasyev.email")
public class EmailApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(EmailApplication.class, args);

        EmailService emailService = context.getBean(EmailService.class);

//         1 email with file
//         emailService.sendMimeMessage("00kirill.afanasyev00@gmail.com", "Hello world with file!", "Hello world!");

        // 1 templated-email with gif
//        emailService.sendMessageUsingThymeleafTemplate("kirill_afanasyev00@mail.ru", "Html email message",
//            Template.LOVE);
    }
}
