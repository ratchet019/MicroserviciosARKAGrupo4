package com.arka.ecommerce.msproducts.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoRequest {

    @NotNull
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotNull(message = "Reorder point cannot be null")
    private Integer reorderPoint;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull(message = "Created at cannot be null")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
