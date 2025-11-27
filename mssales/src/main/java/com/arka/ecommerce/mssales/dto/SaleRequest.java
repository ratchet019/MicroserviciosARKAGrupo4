package com.arka.ecommerce.mssales.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    private String customerName;
    private Long orderId;
    private List<SaleDetailRequest> details;
}
