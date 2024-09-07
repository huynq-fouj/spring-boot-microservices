package com.kachina.identity_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kachina.identity_service.service.UserService;
import com.kachina.identity_service.dto.request.AuthRequest;
import com.kachina.identity_service.dto.request.UserCreationRequest;
import com.kachina.identity_service.dto.response.AuthResponse;
import com.kachina.identity_service.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return userService.getUser(authRequest);
    }

    @PostMapping("/sign-up")
    public UserResponse signup(@RequestBody UserCreationRequest userCreationRequest) {
        return userService.createUser(userCreationRequest);
    }

}