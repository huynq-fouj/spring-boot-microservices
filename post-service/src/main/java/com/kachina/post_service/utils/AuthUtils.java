package com.kachina.post_service.utils;

import org.springframework.util.StringUtils;

public class AuthUtils {
    
    public static String parseJwt(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
