package com.arka.orderservice.controller;

import com.arka.orderservice.dto.*;
import com.arka.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Mono<ResponseEntity<OrderResponse>> create(@Valid @RequestBody Mono<OrderRequest> reqMono) {
        return reqMono.flatMap(req -> service.createOrder(req))
                .map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp));
    }

    @PutMapping("/{id}/confirm")
    public Mono<ResponseEntity<OrderResponse>> confirm(@PathVariable("id") Long id) {
        return service.confirmOrder(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderResponse>> get(@PathVariable("id") Long id) {
        return service.getOrderSummary(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderResponse>> updateOrder(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderRequest request) {

        return service.updateOrder(id, request)
                .map(updatedOrder -> ResponseEntity.ok(updatedOrder))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                    }
                    return Mono.error(e);
                });
    }



}
