package com.kachina.identity_service.service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import feign.FeignException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kachina.identity_service.dto.request.*;
import com.kachina.identity_service.dto.response.*;
import com.kachina.identity_service.enums.*;
import com.kachina.identity_service.jwt.*;
import com.kachina.identity_service.repository.*;
import com.kachina.identity_service.repository.httpClient.ProfileClient;
import com.kachina.identity_service.entity.*;
import com.kachina.identity_service.mapper.*;
import com.kachina.identity_service.producer.UserProducer;
import com.kachina.identity_service.exception.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileClient profileClient;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserProducer userProducer;
    private final ExecutorService executorService;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username \"" + request.getUsername() + "\" already exists.");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(encoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        Set<Role> roles = new HashSet<>();

        Optional<Role> role = roleRepository.findByName(ERole.USER);

        if(role.isPresent()) {
            roles.add(role.get());
        }else {
            Role newRole = new Role();
            newRole.setName(ERole.USER);
            newRole = roleRepository.save(newRole);
            roles.add(newRole);
        }

        newUser.setRoles(roles);
        newUser = userRepository.save(newUser);

        ProfileCreationRequest profileRequest = UserMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(newUser.getId());
        ProfileResponse profileResponse = profileClient.createProfile(profileRequest);

        userProducer.sendUserCreationNotification(newUser);

        return UserMapper.toUserResponse(newUser, profileResponse);
    }

    public AuthResponse getUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        var profileResponse = profileClient.getProfile(user.getId());

        return AuthResponse.builder()
            .accessToken(jwtUtils.generateToken(user))
            .user(UserMapper.toUserResponse(user, profileResponse))
            .build();
    }

    public UserResponse getMyInfor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        var profileResponse = profileClient.getProfile(user.getId());
        return UserMapper.toUserResponse(user, profileResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) throw new NotFoundException("User not found with id: " + userId);
        ProfileResponse profileResponse = profileClient.getProfile(user.get().getId());
        return UserMapper.toUserResponse(user.get(), profileResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = users.stream()
            .map(user -> toUserResponse(user))
            .collect(Collectors.toList());
        return response;
    }

    private UserResponse toUserResponse(User user) {

        ProfileResponse profileResponse = new ProfileResponse();

        try {
            profileResponse = profileClient.getProfile(user.getId());
        } catch (FeignException | RestClientException e) {
            log.error("Profile not found with id: {}", user.getId());
        }

        return UserMapper.toUserResponse(
            user, 
            profileResponse
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersV2() {
        List<User> users = userRepository.findAll();

        List<CompletableFuture<UserResponse>> futures = users.stream()
                .map(user -> toUserResponseAsync(user))
                .collect(Collectors.toList());

        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    @Async
    private CompletableFuture<UserResponse> toUserResponseAsync(User user) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            ProfileResponse profileResponse = new ProfileResponse();
            try {
                profileResponse = profileClient.getProfile(user.getId());
            } catch (FeignException | RestClientException ex) {
                logErrors(ex, user);
            }
            return UserMapper.toUserResponse(user, profileResponse);
        });
    }

    private void logErrors(Throwable ex, User user) {
        if (ex instanceof FeignException) {
            FeignException feignEx = (FeignException) ex;
            log.error("FeignException occurred with status: " + feignEx.status() + " for user: " + user.getId(), feignEx);
        } else {
            log.error("An error occurred for user: " + user.getId(), ex);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsersV3() {
        List<User> users = userRepository.findAll();

        List<String> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());

        List<ProfileResponse> profileResponses = profileClient.getProfileByIds(userIds);

        List<UserResponse> response = users.stream()
                .map(user -> {
                    ProfileResponse profile = profileResponses.stream()
                            .filter(p -> p.getUserId().equals(user.getId()))
                            .findFirst()
                            .orElse(new ProfileResponse());
                    return UserMapper.toUserResponse(user, profile);
                })
                .collect(Collectors.toList());
        return response;
    }

}
