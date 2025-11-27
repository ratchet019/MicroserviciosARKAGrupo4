package com.arka.ecommerce.msmovement.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequest {
    private Long productId;
    @NotBlank(message = "movementType no puede estar vacío")
    private String movementType; // IN, OUT, ADJ
    private Integer delta; // puede ser positivo o negativo
    private String reference;
}
