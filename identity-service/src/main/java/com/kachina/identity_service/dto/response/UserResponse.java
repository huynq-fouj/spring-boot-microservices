package com.kachina.identity_service.dto.response;

import java.util.*;
import java.time.*;
import lombok.*;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private LocalDate dob;
    private List<String> roles;

}
