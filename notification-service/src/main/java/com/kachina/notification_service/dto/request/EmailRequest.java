package com.kachina.notification_service.dto.request;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    
    private Sender sender;
    private List<Recipient> to;
    private String htmlContent;
    private String subject;

}
