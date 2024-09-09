package com.kachina.api_gateway.config;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachina.api_gateway.dto.response.ApiResponse;
import com.kachina.api_gateway.service.IdentityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final String TOKEN_PREFIX = "Bearer ";
    private final IdentityService identityService;
    private final ObjectMapper objectMapper;
    private final PathMatcher pathMatcher;

    private final String[] PUBLIC_ENDPOINT = {
        "/identity/auth/**"
    };

    @Value("${app.api-prefix}")
    private String apiPrefix;

    @Override
    public int getOrder() {
        // Higher than other filter
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //Check public endpoint
        if(isPublicEndPoint(exchange.getRequest())) return chain.filter(exchange);
        // Get token form authorization header
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeaders)) return unauthentication(exchange.getResponse());
        String token = authHeaders.getFirst().replace(TOKEN_PREFIX, "");
        
        return identityService.verifyToken(token).flatMap(res -> {
            // Verify token
            if(res.isValid()) return chain.filter(exchange);
            else return unauthentication(exchange.getResponse());
        }).onErrorResume(throwable -> unauthentication(exchange.getResponse()));
    }

    public Mono<Void> unauthentication(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
            .code(HttpStatus.UNAUTHORIZED.value())
            .message("Unauthenticated")
            .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            log.error("Can not write value as string:", e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    private boolean isPublicEndPoint(ServerHttpRequest request) {
        String requestPath = request.getURI().getPath();
        return Arrays.stream(PUBLIC_ENDPOINT).anyMatch(s -> pathMatcher.match(apiPrefix + s, requestPath));
    }
    
}
