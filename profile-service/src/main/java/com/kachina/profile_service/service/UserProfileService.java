package com.kachina.profile_service.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.kachina.profile_service.dto.request.UserProfileCreationRequest;
import com.kachina.profile_service.dto.response.UserProfileResponse;
import com.kachina.profile_service.entity.UserProfile;
import com.kachina.profile_service.mapper.UserProfileMapper;
import com.kachina.profile_service.repositoty.UserProfileRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileResponse createOrUpdateUserProfile(UserProfileCreationRequest request) {
        String userId = request.getUserId();
        if (userId == null || userId.equals("")) throw new RuntimeException("\"userId\" is required");
        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile.isPresent()) {
            UserProfile updUserProfile = userProfileMapper.toUserProfile(request);
            updUserProfile.setId(userProfile.get().getId());
            updUserProfile = userProfileRepository.save(updUserProfile);
            return userProfileMapper.toUserProfileResponse(updUserProfile);
        }
        UserProfile newUserProfile = userProfileMapper.toUserProfile(request);
        newUserProfile = userProfileRepository.save(newUserProfile);
        return userProfileMapper.toUserProfileResponse(newUserProfile);
    }

    public UserProfileResponse getUserProfile(String userId) {
        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found!"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public boolean deleteUserProfile(String userId) {
        UserProfile userProfile = userProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found!"));
        userProfileRepository.delete(userProfile);
        return true;
    }
}
