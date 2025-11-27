package com.arka.ecommerce.mssales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleReportDto {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private BigDecimal totalSales;
    private Integer totalOrders;
}
