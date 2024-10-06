package com.kachina.profile_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kachina.profile_service.dto.request.UserProfileCreationRequest;
import com.kachina.profile_service.dto.response.UserProfileResponse;
import com.kachina.profile_service.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/create")
    public UserProfileResponse createProfile(@RequestBody UserProfileCreationRequest request) {
        return userProfileService.createOrUpdateUserProfile(request);
    }

    @GetMapping("/{userId}")
    public UserProfileResponse getProfile(@PathVariable("userId") String userId) {
        return userProfileService.getUserProfile(userId);
    }

    @PutMapping("/update/{userId}")
    public UserProfileResponse updateProfile(
            @PathVariable("userId") String userId, @RequestBody UserProfileCreationRequest request) {
        request.setUserId(userId);
        return userProfileService.createOrUpdateUserProfile(request);
    }

    @DeleteMapping("/delete/{userId}")
    public boolean deleteProfile(@PathVariable("userId") String userId) {
        return userProfileService.deleteUserProfile(userId);
    }

    @PostMapping(value = "/list-by-ids")
    List<UserProfileResponse> getProfileByIds(@RequestBody List<String> userIds) {
        return userProfileService.getUserProfileByIds(userIds);
    }
}
