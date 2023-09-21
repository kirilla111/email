package ru.afanasyev.email.adapter.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.afanasyev.email.app.EmailService;

@Controller
@RequestMapping("/preview")
@RequiredArgsConstructor
@Slf4j
public class PreviewController {
    @GetMapping
    public String preview() {
        log.info("preview email template");

        return "index";
    }
}
