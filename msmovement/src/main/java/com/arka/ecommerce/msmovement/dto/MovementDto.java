package com.arka.ecommerce.msmovement.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {
    private Long id;
    private Long productId;
    private String movementType;
    private Integer delta;
    private String reference;
    private LocalDateTime createdAt;
}

