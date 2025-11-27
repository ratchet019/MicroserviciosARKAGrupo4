package com.arka.ecommerce.msproducts.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import com.arka.ecommerce.msproducts.dto.ProductDtoRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoResponse;
import com.arka.ecommerce.msproducts.model.Product;
import com.arka.ecommerce.msproducts.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private ProductService productService;

    private Product sampleProduct;
    private ProductDtoRequest sampleRequest;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        productService = new ProductService(productRepository, webClientBuilder);

        sampleProduct = new Product(1L, "Laptop", "Gaming Laptop", 1200.0, 10L, 5, "ACTIVE", LocalDateTime.now());
        sampleRequest = new ProductDtoRequest(1L, "Laptop", "Gaming Laptop", 1200.0, 10L, 5, "ACTIVE", LocalDateTime.now());
    }

    @Test
    void findProductById_ReturnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleProduct));

        Mono<ProductDtoResponse> result = productService.findProductById(1L);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals("Laptop"))
                .verifyComplete();
    }

    @Test
    void findProductById_NotFound_ReturnsError() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<ProductDtoResponse> result = productService.findProductById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && ex.getMessage().equals("Product not found"))
                .verify();
    }

    @Test
    void findAllProducts_ReturnsFlux() {
        when(productRepository.findAll()).thenReturn(Flux.just(sampleProduct));

        StepVerifier.create(productService.findAllProducts())
                .expectNextMatches(dto -> dto.getName().equals("Laptop"))
                .verifyComplete();
    }

    @Test
    void findByCategoryId_ReturnsFlux() {
        when(productRepository.findByCategoryId(10L)).thenReturn(Flux.just(sampleProduct));

        StepVerifier.create(productService.findByCategoryId(10L))
                .expectNextMatches(dto -> dto.getCategoryId().equals(10L))
                .verifyComplete();
    }

    @Test
    void updateProduct_ReturnsUpdatedProduct() {
        Product updated = new Product(1L, "Laptop Pro", "Updated desc", 1500.0, 10L, 5, "ACTIVE", LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updated));

        Mono<ProductDtoResponse> result = productService.updateProduct(1L, sampleRequest);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getName().equals("Laptop Pro") || dto.getName().equals("Laptop"))
                .verifyComplete();
    }

    @Test
    void updateProduct_NotFound_ReturnsError() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<ProductDtoResponse> result = productService.updateProduct(1L, sampleRequest);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && ex.getMessage().equals("Product not found with id: 1"))
                .verify();
    }

    @Test
    void deleteProduct_Exists_ReturnsVoid() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(sampleProduct));
        when(productRepository.delete(sampleProduct)).thenReturn(Mono.empty());

        Mono<Void> result = productService.delete(1L);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void deleteProduct_NotExists_ReturnsEmptyMono() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = productService.delete(1L);

        StepVerifier.create(result).verifyComplete();
    }
}
