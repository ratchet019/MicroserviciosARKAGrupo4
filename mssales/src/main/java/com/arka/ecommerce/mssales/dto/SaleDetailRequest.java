package com.arka.ecommerce.mssales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
