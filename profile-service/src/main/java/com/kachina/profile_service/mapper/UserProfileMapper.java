package com.kachina.profile_service.mapper;

import org.mapstruct.Mapper;

import com.kachina.profile_service.dto.request.UserProfileCreationRequest;
import com.kachina.profile_service.dto.response.UserProfileResponse;
import com.kachina.profile_service.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfile toUserProfile(UserProfileCreationRequest userProfileCreationRequest);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
