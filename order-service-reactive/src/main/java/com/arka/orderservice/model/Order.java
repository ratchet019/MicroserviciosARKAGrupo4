package com.arka.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orders")
public class Order {
    @Id
    private Long id;

    @Column("order_date")
    private LocalDateTime orderDate;

    @Column("customer_name")
    private String customerName;

    private String status;

    @Column("total_amount")
    private BigDecimal totalAmount;
}
