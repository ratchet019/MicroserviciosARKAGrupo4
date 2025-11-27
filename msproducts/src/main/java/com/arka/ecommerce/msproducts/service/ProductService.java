package com.arka.ecommerce.msproducts.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.arka.ecommerce.msproducts.dto.CategoryDto;
import com.arka.ecommerce.msproducts.dto.InventoryInitRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoResponse;
import com.arka.ecommerce.msproducts.mapper.ProductMapper;
import com.arka.ecommerce.msproducts.model.Product;
import com.arka.ecommerce.msproducts.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final WebClient webClient; // configuracion para comunicarse con otros microservicios

    public ProductService(ProductRepository productRepository, WebClient.Builder webClientBuilder) {
        this.productRepository = productRepository;
        this.webClient = webClientBuilder.baseUrl("http://category-service:8081").build();
    }

    public Mono<ProductDtoResponse> createProduct(ProductDtoRequest productDtoRequest) {
        // 1) Valido que la categoria exista via category-service
        return webClient.get()
                .uri("/api/v1/categories/{id}", productDtoRequest.getCategoryId())
                .retrieve()
                .bodyToMono(CategoryDto.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Category not found")))
                .flatMap(cat -> productRepository
                        .save(new Product(null, productDtoRequest.getName(), productDtoRequest.getDescription(),
                                productDtoRequest.getPrice(), productDtoRequest.getCategoryId(),
                                productDtoRequest.getReorderPoint(), productDtoRequest.getStatus(),
                                productDtoRequest.getCreatedAt())))
                .flatMap(saved -> {
                    // 2) Crear stock inicial en inventory-service
                    InventoryInitRequest init = new InventoryInitRequest(saved.getId(), 0);
                    return webClient.mutate().baseUrl("http://inventory-service:8082").build()
                            .post().uri("/api/v1/inventory")
                            .bodyValue(init)
                            .retrieve()
                            .onStatus(
                                    status -> status.is4xxClientError() || status.is5xxServerError(),
                                    response -> response.bodyToMono(String.class)
                                            .flatMap(errorBody -> Mono.error(new RuntimeException(
                                                    "There is an error on inventory service" + errorBody))))
                            .bodyToMono(Void.class)
                            .then(Mono.just(ProductMapper.toDto(saved)));
                });
    }

    public Mono<ProductDtoResponse> findProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDto)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
    }

    public Flux<ProductDtoResponse> findAllProducts() {
        return productRepository.findAll()
                .map(ProductMapper::toDto)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No products found")));
    }

    public Flux<ProductDtoResponse> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .map(ProductMapper::toDto)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No products found for category")));
    }

    public Mono<ProductDtoResponse> updateProduct(Long id, ProductDtoRequest productDtoRequest) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found with id: " + id)))
                .flatMap(existingProduct -> {
                    // Actualizar los campos del producto existente
                    existingProduct.setName(productDtoRequest.getName());
                    existingProduct.setDescription(productDtoRequest.getDescription());
                    existingProduct.setPrice(productDtoRequest.getPrice());
                    existingProduct.setCategoryId(productDtoRequest.getCategoryId());
                    existingProduct.setReorderPoint(productDtoRequest.getReorderPoint());
                    existingProduct.setStatus(productDtoRequest.getStatus());
                    existingProduct.setCreatedAt(productDtoRequest.getCreatedAt());
                    return productRepository.save(existingProduct);
                })
                .map(ProductMapper::toDto);

    }

    public Mono<Void> delete(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.empty()) // Si no existe, retorna Mono.empty()
                .flatMap(product -> productRepository.delete(product)); // Si existe, elimina y retorna Mono<Void>
    }

}
