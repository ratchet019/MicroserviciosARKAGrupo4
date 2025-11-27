package com.arka.ecommerce.mssales.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("sales_header")
public class SaleHeader {

    @Id
    private Long id;
    @Column("sale_date")
    private LocalDateTime saleDate;
    @Column("total_amount")
    private BigDecimal totalAmount;
    @Column("customer_name")
    private String customerName;
    @Column("order_id")
    private Long orderId;
    private String status;

}
