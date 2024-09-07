package com.kachina.identity_service.config;

import java.util.*;
import java.time.LocalDate;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kachina.identity_service.repository.RoleRepository;
import com.kachina.identity_service.repository.UserRepository;
import com.kachina.identity_service.repository.httpClient.ProfileClient;
import com.kachina.identity_service.entity.Role;
import com.kachina.identity_service.entity.User;
import com.kachina.identity_service.enums.ERole;
import com.kachina.identity_service.dto.request.ProfileCreationRequest;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitial {
    
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final ProfileClient profileClient;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    public ApplicationRunner initialData() {
        return args -> {
            log.info("Start Init Data...");
            Role roleUser = getRole(ERole.USER);
            Role roleAdmin = getRole(ERole.ADMIN);
            log.info("Initial Admin Account...");
            if(!userRepository.findByUsername(ADMIN_USER_NAME).isPresent()) {
                User admin = new User();
                admin.setUsername(ADMIN_USER_NAME);
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                Set<Role> roles = new HashSet<>();
                roles.add(roleUser);
                roles.add(roleAdmin);
                admin.setRoles(roles);
                admin = userRepository.save(admin);
                profileClient.createProfile(ProfileCreationRequest.builder()
                    .userId(admin.getId())
                    .firstName("John")
                    .lastName("Doe")
                    .dob(LocalDate.of(2000,1,1))
                    .city("Ha Noi")
                    .build()
                );
                log.info("Admin Account Has Been Created![username: " + ADMIN_USER_NAME + "; password: " + ADMIN_PASSWORD + "]");
            } else {
                log.info("Admin Account Already Exists![username: " + ADMIN_USER_NAME + "; password: " + ADMIN_PASSWORD + "]");
            }
        };
    }

    public Role getRole(ERole roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if(!role.isPresent()) return createRole(roleName);
        else return role.get();
    }

    public Role createRole(ERole roleName) {
        log.info("Create Role " + roleName.name());
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

}
