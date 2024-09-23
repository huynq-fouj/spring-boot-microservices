package com.kachina.identity_service.mapper;

import java.util.stream.Collectors;
import com.kachina.identity_service.dto.response.*;
import com.kachina.identity_service.dto.request.*;
import com.kachina.identity_service.entity.*;

public class UserMapper {
    
    private UserMapper() {}

    public static UserResponse toUserResponse(User user, ProfileResponse profile) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(profile.getFirstName())
            .lastName(profile.getLastName())
            .dob(profile.getDob())
            .city(profile.getCity())
            .roles(user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList()))
            .build();
    }

    public static ProfileCreationRequest toProfileCreationRequest(UserCreationRequest req) {
        return ProfileCreationRequest.builder()
            .firstName(req.getFirstName())
            .lastName(req.getLastName())
            .dob(req.getDob())
            .city(req.getCity())
            .build();
    }

}
