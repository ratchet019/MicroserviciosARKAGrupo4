package com.arka.ecommerce.msproducts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arka.ecommerce.msproducts.dto.ProductDtoRequest;
import com.arka.ecommerce.msproducts.dto.ProductDtoResponse;
import com.arka.ecommerce.msproducts.service.ProductService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<ResponseEntity<ProductDtoResponse>> createProduct(
            @Valid @RequestBody ProductDtoRequest productDtoRequest) {
        return productService.createProduct(productDtoRequest)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDtoResponse>> getProductById(@Valid @PathVariable Long id) {
        return productService.findProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public Flux<ProductDtoResponse> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public Flux<ProductDtoResponse> getProductsByCategoryId(@Valid @PathVariable Long categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDtoResponse>> updateProduct(@Valid @PathVariable Long id,
            @RequestBody ProductDtoRequest productDtoRequest) {
        return productService.updateProduct(id, productDtoRequest)
                .map(updated -> ResponseEntity.ok(updated))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@Valid @PathVariable Long id) {
        return productService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build())) 
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())); 
                                                                                                     
    }

}
