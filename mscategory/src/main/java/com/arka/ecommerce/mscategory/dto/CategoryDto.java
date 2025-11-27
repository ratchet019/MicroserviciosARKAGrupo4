package com.arka.ecommerce.mscategory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotNull
    private Long id;

    @NotNull(message="Name cannot be null")
    private String name;

    @NotNull(message="Description cannot be null")
    private String description;
}
