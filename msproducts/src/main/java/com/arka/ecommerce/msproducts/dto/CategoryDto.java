package com.arka.ecommerce.msproducts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotBlank
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;
}
