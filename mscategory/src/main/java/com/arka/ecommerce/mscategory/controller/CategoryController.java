package com.arka.ecommerce.mscategory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arka.ecommerce.mscategory.dto.CategoryDto;
import com.arka.ecommerce.mscategory.service.CategoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Crear categoría
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    // Obtener todas las categorías
    @GetMapping
    public Flux<CategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    // Obtener una categoría por ID
    @GetMapping("/{id}")
    public Mono<CategoryDto> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    // Actualizar una categoría
    @PutMapping("/{id}")
    public Mono<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    // Eliminar una categoría
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

}
