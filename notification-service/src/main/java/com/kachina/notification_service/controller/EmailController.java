package com.kachina.notification_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kachina.notification_service.dto.request.SendEmailRequest;
import com.kachina.notification_service.dto.response.EmailResponse;
import com.kachina.notification_service.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    
    @PostMapping("/send")
    public EmailResponse sendEmail(@RequestBody SendEmailRequest request) {
        return emailService.sendEmail(request);
    }

}
