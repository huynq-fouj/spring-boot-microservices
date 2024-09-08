package com.kachina.identity_service.service;

import org.springframework.stereotype.Service;

import com.kachina.identity_service.dto.request.VerifyTokenRequest;
import com.kachina.identity_service.dto.response.VerifyTokenResponse;
import com.kachina.identity_service.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;

    public VerifyTokenResponse verifyToken(VerifyTokenRequest verifyTokenRequest) {
        var token = verifyTokenRequest.getToken();
        return VerifyTokenResponse.builder()
            .valid(jwtUtils.validateJwtToken(token))
            .userId(jwtUtils.getUserId(token))
            .build();
    }

}
