package com.arka.ecommerce.msmovement.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("movements")
public class Movements {
    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("movement_type")
    private String movementType; // IN, OUT, ADJ

    private Integer delta;

    private String reference;

    @Column("created_at")
    private LocalDateTime createdAt;
}
