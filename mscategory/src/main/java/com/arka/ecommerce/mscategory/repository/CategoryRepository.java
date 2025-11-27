package com.arka.ecommerce.mscategory.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.arka.ecommerce.mscategory.model.Category;

import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Long>{

    Mono<Category> findByName(String name);

}
