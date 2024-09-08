package com.kachina.api_gateway.dto.response;

import lombok.Data;

@Data
public class VerifyTokenResponse {
    private boolean valid;
    private String userId;
}
