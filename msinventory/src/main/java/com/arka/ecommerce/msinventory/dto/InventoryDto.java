package com.arka.ecommerce.msinventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {

    @NotNull
    private Long productId;

    @NotNull
    @Min(message = "Quantity must be at least 1", value = 1)
    private Integer quantity;

    @NotNull
    private String skuCode;
}
