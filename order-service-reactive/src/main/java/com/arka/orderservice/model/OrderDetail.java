package com.arka.orderservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_detail")
public class OrderDetail {
    @Id
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("product_id")
    private Long productId;

    private Integer quantity;

    @Column("unit_price")
    private BigDecimal unitPrice;
}
