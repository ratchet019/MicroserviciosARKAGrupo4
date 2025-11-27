package com.arka.ecommerce.mssales.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // Llama al micro de movimientos
    @Bean
    public WebClient movementWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://msmovement:8084").build();

    }

    // Llama al micro de inventario
    @Bean
    public WebClient inventoryWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://msinventory:8082").build();

    }

    // Si tuvieras otro micro de órdenes, ajustá el nombre también
    @Bean
    public WebClient orderWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://msorder:8085").build();

    }
}
