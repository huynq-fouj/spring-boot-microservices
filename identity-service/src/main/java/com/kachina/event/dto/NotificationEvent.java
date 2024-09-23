package com.kachina.event.dto;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    
    private String channel;
    private String recipient;
    private String templateCode;
    private Map<String, Object> params;
    private String subject;
    private String body;

}
