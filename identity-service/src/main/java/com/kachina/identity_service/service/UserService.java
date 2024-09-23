package com.kachina.identity_service.service;

import java.util.*;
import java.util.stream.*;

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

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileClient profileClient;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserProducer userProducer;

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
            .map(user -> UserMapper.toUserResponse(
                user, 
                profileClient.getProfile(user.getId()))
            ).collect(Collectors.toList());
        return response;
    }

}
