package com.kachina.api_gateway.dto.response;

import java.util.*;

import lombok.Data;

@Data
public class VerifyTokenResponse {
    private boolean valid;
    private String userId;
    private List<String> roles;
}
