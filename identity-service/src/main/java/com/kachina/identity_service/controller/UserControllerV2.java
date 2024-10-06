package com.kachina.identity_service.controller;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kachina.identity_service.service.UserService;
import com.kachina.identity_service.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerV2 {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsersV2();
    }

}
