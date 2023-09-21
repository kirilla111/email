package ru.afanasyev.email.adapter.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.afanasyev.email.app.EmailService;

@RestController("/")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final EmailService emailService;

    @PostMapping
    public void notify(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        log.info("sending email to: {}, text: {}", to, text);
        emailService.sendSimpleMessage(to, subject, text);
    }
}
