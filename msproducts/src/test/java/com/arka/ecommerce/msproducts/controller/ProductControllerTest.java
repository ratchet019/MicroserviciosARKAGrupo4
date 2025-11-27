package com.arka.ecommerce.msproducts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.arka.ecommerce.msproducts.dto.ProductDtoRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoResponse;
import com.arka.ecommerce.msproducts.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ProductController.class)
public class ProductControllerTest {

        @Autowired
        private WebTestClient webTestClient;

        @SuppressWarnings("removal")
        @MockBean
        private ProductService productService;

        private ProductDtoRequest productDtoRequest;
        private ProductDtoResponse productDtoResponse;

        @BeforeEach
        @SuppressWarnings("unused")
        void setUp() {
                productDtoRequest = new ProductDtoRequest();
                productDtoRequest.setId(1L);
                productDtoRequest.setName("Test Product");
                productDtoRequest.setDescription("Test Description");
                productDtoRequest.setPrice(10.0);
                productDtoRequest.setCategoryId(1L);
                productDtoRequest.setReorderPoint(5);
                productDtoRequest.setStatus("ACTIVE");
                productDtoRequest.setCreatedAt(java.time.LocalDateTime.now());

                productDtoResponse = new ProductDtoResponse();
                productDtoResponse.setName("Test Response");
                productDtoResponse.setDescription("Test Description Response");
                productDtoResponse.setPrice(15.0);
                productDtoResponse.setCategoryId(2L);
                productDtoResponse.setCreatedAt(java.time.LocalDateTime.now().withNano(0));
        }

        @Test
        void createProduct_ReturnsCreated() {
                Mockito.when(productService.createProduct(Mockito.any()))
                                .thenReturn(Mono.just(productDtoResponse));

                webTestClient.post()
                                .uri("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(productDtoRequest)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(ProductDtoResponse.class)
                                .consumeWith(response -> {
                                        ProductDtoResponse body = response.getResponseBody();
                                        assertNotNull(body);
                                        assertEquals(productDtoResponse.getName(), body.getName());
                                        // ...otros asserts...
                                });
        }

        @Test
        void getProductById_ReturnsOk() {
                Mockito.when(productService.findProductById(1L))
                                .thenReturn(Mono.just(productDtoResponse));

                webTestClient.get()
                                .uri("/api/v1/products/1")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(ProductDtoResponse.class)
                                .isEqualTo(productDtoResponse);
        }

        @Test
        void getProductById_NotFound() {
                Mockito.when(productService.findProductById(2L))
                                .thenReturn(Mono.empty());

                webTestClient.get()
                                .uri("/api/v1/products/2")
                                .exchange()
                                .expectStatus().isNotFound();
        }

        @Test
        void getAllProducts_ReturnsOk() {
                Mockito.when(productService.findAllProducts())
                                .thenReturn(Flux.just(productDtoResponse));

                webTestClient.get()
                                .uri("/api/v1/products")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(ProductDtoResponse.class)
                                .hasSize(1);
        }

        @Test
        void getProductsByCategoryId_ReturnsOk() {
                Mockito.when(productService.findByCategoryId(1L))
                                .thenReturn(Flux.just(productDtoResponse));

                webTestClient.get()
                                .uri("/api/v1/products/category/1")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(ProductDtoResponse.class)
                                .hasSize(1);
        }

        @Test
        void updateProduct_ReturnsOk() {
                Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any()))
                                .thenReturn(Mono.just(productDtoResponse));

                webTestClient.put()
                                .uri("/api/v1/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(productDtoRequest)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(ProductDtoResponse.class)
                                .isEqualTo(productDtoResponse);
        }

        @Test
        void deleteProduct_ReturnsNoContent() {
                Mockito.when(productService.delete(1L))
                                .thenReturn(Mono.empty());

                webTestClient.delete()
                                .uri("/api/v1/products/1")
                                .exchange()
                                .expectStatus().isNoContent();
        }

        @Test
        void deleteProduct_ReturnsNotFound() {
                Mockito.when(productService.delete(99L))
                                .thenReturn(Mono.error(new RuntimeException("Product not found"))); 

                webTestClient.delete()
                                .uri("/api/v1/products/99")
                                .exchange()
                                .expectStatus().isNotFound();
        }
}
