package com.kachina.identity_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kachina.identity_service.dto.request.ProfileCreationRequest;
import com.kachina.identity_service.dto.response.ProfileResponse;

@FeignClient(
    name = "profile-service",
    url = "${app.services.profile.url}"
)
public interface ProfileClient {

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse createProfile(@RequestBody ProfileCreationRequest request);

    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse updateProfile(@PathVariable("userId") String userId, @RequestBody ProfileCreationRequest request);

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getProfile(@PathVariable("userId") String userId);

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse deleteProfile(@PathVariable("userId") String userId);
    
}
