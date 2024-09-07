package com.kachina.identity_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private short code;
    private String message;
    private T result;

}
