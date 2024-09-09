package com.kachina.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.kachina.api_gateway.repository.IdentityClient;

@Configuration
public class WebClientConfig {

    @Value("${app.services.identity.url}")
    private String identityUrl;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
            .baseUrl(identityUrl)
            .build();
    }

    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }

}