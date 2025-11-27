package com.arka.ecommerce.msproducts.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.arka.ecommerce.msproducts.model.Product;
import reactor.core.publisher.Flux;


public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Flux<Product> findByCategoryId(Long categoryId);
    
}
