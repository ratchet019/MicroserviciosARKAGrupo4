package com.arka.ecommerce.mssales.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long id;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private String customerName;
    private String status;
    private List<SaleDetailRequest> details;
}