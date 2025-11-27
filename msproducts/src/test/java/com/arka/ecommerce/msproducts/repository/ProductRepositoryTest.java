package com.arka.ecommerce.msproducts.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arka.ecommerce.msproducts.model.Product;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void findByCategoryId_ReturnsFlux() {
        Product p = new Product();
        p.setCategoryId(1L);
        p.setName("Monitor");

        Mockito.when(productRepository.findByCategoryId(1L))
                .thenReturn(Flux.just(p));

        StepVerifier.create(productRepository.findByCategoryId(1L))
                .expectNext(p)
                .verifyComplete();
    }
}
