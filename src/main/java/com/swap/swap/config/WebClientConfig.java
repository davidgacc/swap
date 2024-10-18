package com.swap.swap.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @Qualifier("githubWebClient")
    public WebClient githubWebClient(@Value("${github.api.base-url}") String apiURI) {
        return WebClient.builder().baseUrl(apiURI).build();
    }
}
