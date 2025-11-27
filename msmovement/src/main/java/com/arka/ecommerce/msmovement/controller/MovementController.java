package com.arka.ecommerce.msmovement.controller;

import com.arka.ecommerce.msmovement.dto.MovementDto;
import com.arka.ecommerce.msmovement.dto.MovementRequest;
import com.arka.ecommerce.msmovement.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService service;

    /**
     * HU-MOV-01: Registrar movimiento
     */

    @PostMapping
    public Mono<ResponseEntity<MovementDto>> createMovement(@Valid @RequestBody MovementRequest request) {
        return service.registerMovement(request)
                .map(m -> ResponseEntity.status(HttpStatus.CREATED).body(m));
    }



    /**
     * HU-MOV-02: Consultar todos los movimientos (con filtros opcionales)
     */
    @GetMapping
    public Flux<MovementDto> listMovements(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        return service.getMovements(productId, type, from, to);
    }

    /**
     * HU-MOV-03: Detalle de un movimiento
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementDto>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * HU-MOV-04: Histórico por producto
     */
    @GetMapping("/product/{productId}")
    public Flux<MovementDto> getByProduct(@PathVariable Long productId) {
        return service.getMovementsByProduct(productId);
    }

    /**
     * HU-MOV-05: Anular movimiento (crea movimiento compensatorio)
     */
    @PostMapping("/{id}/cancel")
    public Mono<ResponseEntity<MovementDto>> cancelMovement(@PathVariable Long id) {
        return service.cancelMovement(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
