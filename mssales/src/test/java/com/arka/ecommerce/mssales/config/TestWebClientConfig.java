package com.arka.ecommerce.mssales.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestWebClientConfig {

    @Bean
    public WebClient webClient() {
        // simple cliente de prueba — o podés devolver Mockito.mock(WebClient.class)
        return WebClient.builder().baseUrl("http://localhost").build();
    }
}
