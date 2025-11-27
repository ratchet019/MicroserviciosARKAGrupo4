package com.arka.ecommerce.msproducts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInitRequest {

    @NotBlank
    private Long productId;

    @NotBlank
    @Min(value = 0, message = "Initial quantity must be zero or greater")
    private Integer initialQuantity;
}
