package com.kachina.notification_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kachina.notification_service.dto.request.EmailRequest;
import com.kachina.notification_service.dto.request.SendEmailRequest;
import com.kachina.notification_service.dto.request.Sender;
import com.kachina.notification_service.dto.response.EmailResponse;
import com.kachina.notification_service.exception.AppException;
import com.kachina.notification_service.repository.httpClient.EmailClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final EmailClient emailClient;
    @Value("${brevo.apiKey}")
    private String apiKey;
    @Value("${brevo.sender.email}")
    private String senderEmail;
    @Value("${brevo.sender.name}")
    private String senderName;

    public EmailResponse sendEmail(SendEmailRequest request) {

        EmailRequest emailRequest = EmailRequest.builder()
            .sender(Sender.builder()
                .email(senderEmail)
                .name(senderName)
                .build()
            ).to(List.of(request.getTo()))
            .htmlContent(request.getHtmlContent())
            .subject(request.getSubject())
            .build();

        try {
            return emailClient.sendEmail(apiKey, emailRequest);  
        } catch (FeignException e) {
            throw new AppException("Can not send Email!");
        }
    }

}
