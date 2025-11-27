package com.arka.ecommerce.msmovement.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


@Value("${INVENTORY_URL}")
private String inventoryUrl;

@Bean
public WebClient inventoryWebClient(WebClient.Builder builder) {
    return builder.baseUrl(inventoryUrl).build();

}



}