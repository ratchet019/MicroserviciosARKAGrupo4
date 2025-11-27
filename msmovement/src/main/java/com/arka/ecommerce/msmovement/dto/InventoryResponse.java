package com.arka.ecommerce.msmovement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private Long productId;
    private Integer quantity;
}