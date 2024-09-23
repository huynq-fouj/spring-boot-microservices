package com.kachina.notification_service.controller;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;

import com.kachina.notification_service.dto.request.SendEmailRequest;
import com.kachina.notification_service.dto.request.Recipient;
import com.kachina.notification_service.dto.response.EmailResponse;
import com.kachina.notification_service.service.EmailService;
import com.kachina.event.dto.NotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationController {
    
    private final EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent event) {
        EmailResponse res = emailService.sendEmail(SendEmailRequest.builder()
            .to(Recipient.builder()
                .email(event.getRecipient())
                .name(event.getRecipient())
                .build())
            .htmlContent(event.getBody())
            .subject(event.getSubject())
        .build());
        log.info("\u001B[32mMessageId: {}\u001B[0m", res.getMessageId());
    }

}
