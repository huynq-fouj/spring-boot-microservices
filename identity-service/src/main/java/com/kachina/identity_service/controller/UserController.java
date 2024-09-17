package com.kachina.identity_service.controller;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kachina.identity_service.service.UserService;
import com.kachina.identity_service.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/my-info")
    public UserResponse getMyInfor() {
        return userService.getMyInfor();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

}
