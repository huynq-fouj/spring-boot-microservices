package com.kachina.identity_service.producer;

import org.springframework.stereotype.Service;

import org.springframework.kafka.core.KafkaTemplate;

import com.kachina.event.dto.NotificationEvent;
import com.kachina.identity_service.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreationNotification(User user) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
            .channel("EMAIL")
            .recipient(user.getEmail())
            .subject("Welcom Post Kachina!")
            .body("Hello, " + user.getUsername())
            .build();
        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

}
