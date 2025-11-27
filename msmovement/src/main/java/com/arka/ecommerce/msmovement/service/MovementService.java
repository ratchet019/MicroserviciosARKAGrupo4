package com.arka.ecommerce.msmovement.service;

import com.arka.ecommerce.msmovement.dto.InventoryResponse;
import com.arka.ecommerce.msmovement.dto.InventoryUpdateRequest;
import com.arka.ecommerce.msmovement.dto.MovementDto;
import com.arka.ecommerce.msmovement.dto.MovementRequest;
import com.arka.ecommerce.msmovement.entity.Movements;
import com.arka.ecommerce.msmovement.exception.InsufficientStockException;
import com.arka.ecommerce.msmovement.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementRepository repository;
    private final WebClient inventoryWebClient; 

    public Mono<MovementDto> registerMovement(MovementRequest request) {

        // Validar tipo de movimiento
        if (!request.getMovementType().matches("IN|OUT|ADJ")) {
            return Mono.error(new InsufficientStockException("Tipo de movimiento inválido"));
        }

        // Si es salida, verificar stock actual
      if ("OUT".equals(request.getMovementType())) {
    // Consultar stock real en inventory-service
            return inventoryWebClient.get()
                    .uri("/api/v1/inventory/{productId}/stock", request.getProductId())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, res ->
                        Mono.error(new InsufficientStockException("Producto no encontrado en inventario")))
                    .bodyToMono(Integer.class) // suponiendo que el endpoint devuelve solo la cantidad
                    .flatMap(stock -> {
                        if (stock + request.getDelta() < 0) {
                            return Mono.error(new InsufficientStockException("Stock insuficiente"));
                        }
                        return applyInventoryChange(request);
                    });
        } else {
            return applyInventoryChange(request);
        }
    }

    private Mono<MovementDto> applyInventoryChange(MovementRequest request) {
      InventoryUpdateRequest invReq = new InventoryUpdateRequest(request.getProductId(), request.getDelta());
      

    return inventoryWebClient.post()
        .uri("/api/v1/inventory/update")
        .bodyValue(invReq)
        .retrieve()
        .bodyToMono(Void.class) // si update no devuelve nada
        .then(repository.save(new Movements(
                null, request.getProductId(), request.getMovementType(),
                request.getDelta(), request.getReference(), LocalDateTime.now()
        )))
        .map(this::toDto);
    }

    private MovementDto toDto(Movements m) {
        return new MovementDto(m.getId(), m.getProductId(), m.getMovementType(), m.getDelta(), m.getReference(), m.getCreatedAt());
    }

    public Flux<MovementDto> getMovements(Long productId, String type, LocalDate from, LocalDate to) {
        return repository.findAll()
                .filter(m -> (productId == null || m.getProductId().equals(productId)) &&
                        (type == null || m.getMovementType().equalsIgnoreCase(type)) &&
                        (from == null || !m.getCreatedAt().toLocalDate().isBefore(from)) &&
                        (to == null || !m.getCreatedAt().toLocalDate().isAfter(to)))
                .map(this::toDto);
    }

    public Mono<MovementDto> getById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public Flux<MovementDto> getMovementsByProduct(Long productId) {
        return repository.findByProductId(productId).map(this::toDto);
    }

    public Mono<MovementDto> cancelMovement(Long id) {
        return repository.findById(id)
                .flatMap(original -> {
                    // Creamos un movimiento inverso
                    MovementRequest reverse = new MovementRequest(
                            original.getProductId(),
                            original.getMovementType().equals("IN") ? "OUT" : "IN",
                            -original.getDelta(),
                            "CANCEL-" + id
                    );
                    return registerMovement(reverse);
                });
    }

}
