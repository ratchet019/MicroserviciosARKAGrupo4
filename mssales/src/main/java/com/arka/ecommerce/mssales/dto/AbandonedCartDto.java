package com.arka.ecommerce.mssales.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbandonedCartDto {
    private Long orderId;
    private String customerName;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
}
