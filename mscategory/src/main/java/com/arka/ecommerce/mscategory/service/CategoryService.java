package com.arka.ecommerce.mscategory.service;

import org.springframework.stereotype.Service;

import com.arka.ecommerce.mscategory.dto.CategoryDto;
import com.arka.ecommerce.mscategory.model.Category;
import com.arka.ecommerce.mscategory.repository.CategoryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<CategoryDto> create(CategoryDto dto) {
        // validate, idempotency
        return categoryRepository.findByName(dto.getName())
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()))
                .switchIfEmpty(categoryRepository.save(new Category(dto.getId(), dto.getName(), dto.getDescription()))
                        .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription())));
    }

    public Mono<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found with id: " + id)))
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()));
    }

    public Mono<Void> deleteById(Long id) {
        return categoryRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found with id: " + id)));
    }

    public Flux<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("No categories found")))
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()));
    }

    public Mono<CategoryDto> update(Long id, CategoryDto dto) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found with id: " + id)))
                .flatMap(existingCategory -> {
                    existingCategory.setName(dto.getName());
                    existingCategory.setDescription(dto.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getDescription()));
    }

}
