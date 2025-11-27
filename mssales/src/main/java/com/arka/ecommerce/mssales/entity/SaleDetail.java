package com.arka.ecommerce.mssales.entity;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("sales_detail")
public class SaleDetail {

    @Id
    private Long id;

    @Column("sale_id")
    private Long saleId;

    @Column("product_id")
    private Long productId;

    private Integer quantity;

    @Column("unit_price")
    private BigDecimal unitPrice;

    private BigDecimal subtotal;

}
