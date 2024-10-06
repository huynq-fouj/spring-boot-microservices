package com.kachina.identity_service.config;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    
    /**
     * Lấy token trong request gửi lên và thêm Authorization vào fiegnClient nếu có token
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var authToken = servletRequestAttributes.getRequest().getHeader("Authorization");
        if(StringUtils.hasText(authToken)) {
            requestTemplate.header("Authorization", authToken);
        }
    }

}
