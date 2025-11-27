package com.arka.ecommerce.msinventory.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    private Integer quantity;

    @Column("last_updated")
    private LocalDateTime lastUpdated;

    @NotNull
    private String skuCode;

}
