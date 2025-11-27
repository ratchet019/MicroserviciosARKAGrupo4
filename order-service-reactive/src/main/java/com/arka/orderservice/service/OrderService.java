package com.arka.orderservice.service;

import com.arka.orderservice.dto.*;
import com.arka.orderservice.model.*;
import com.arka.orderservice.repository.*;
import com.arka.orderservice.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;

    /**
     * Creates an order and its details reactively.
     */
    public Mono<OrderResponse> createOrder(OrderRequest req) {
        BigDecimal total = OrderMapper.calculateTotal(req.getItems());
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .customerName(req.getCustomerName())
                .status("PENDING")
                .totalAmount(total)
                .build();

        return orderRepository.save(order)
                .flatMap(saved -> {
                    // create details
                    Flux<OrderDetail> detailsFlux = Flux.fromIterable(req.getItems())
                            .map(i -> OrderDetail.builder()
                                    .orderId(saved.getId())
                                    .productId(i.getProductId())
                                    .quantity(i.getQuantity())
                                    .unitPrice(i.getUnitPrice())
                                    .build());
                    return detailRepository.saveAll(detailsFlux).collectList()
                            .map(d -> new OrderResponse(saved.getId(), saved.getStatus(), saved.getTotalAmount()));
                });
    }

    public Mono<OrderResponse> confirmOrder(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(order -> {
                    if (!"PENDING".equals(order.getStatus())) {
                        return Mono.error(new IllegalStateException("Order already processed"));
                    }
                    order.setStatus("CONFIRMED");
                    return orderRepository.save(order);
                })
                .map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getTotalAmount()));
    }

    public Mono<OrderResponse> getOrderSummary(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(order ->
                    detailRepository.findByOrderId(order.getId()).collectList()
                        .map(details -> new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount()))
                );
    }

    // --- método nuevo: actualizar orden (usa OrderRequest y devuelve OrderResponse) ---
    public Mono<OrderResponse> updateOrder(Long id, OrderRequest updatedRequest) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new java.util.NoSuchElementException("Orden no encontrada con ID " + id)))
                .flatMap(existingOrder -> {
                    // validar estado
                    if (!"PENDING".equalsIgnoreCase(existingOrder.getStatus())) {
                        return Mono.error(new IllegalStateException("Solo se pueden modificar órdenes en estado PENDING."));
                    }

                    // recalcular total con los mismos DTOs
                    BigDecimal total = calculateTotal(updatedRequest.getItems());

                    // actualizar campos simples en la entidad Order
                    existingOrder.setCustomerName(updatedRequest.getCustomerName());
                    existingOrder.setTotalAmount(total);

                    // guardar order actualizado
                    return orderRepository.save(existingOrder)
                            .flatMap(savedOrder -> {
                                // eliminar detalles antiguos y crear nuevos detalles con orderId = savedOrder.getId()
                                return detailRepository.deleteByOrderId(savedOrder.getId())
                                        .thenMany(Flux.fromIterable(mapToOrderDetails(savedOrder.getId(), updatedRequest.getItems())))
                                        .flatMap(detailRepository::save)
                                        .collectList()
                                        .map(details -> mapToOrderResponse(savedOrder.getId(), savedOrder.getCustomerName(),
                                                savedOrder.getStatus(), savedOrder.getTotalAmount(), updatedRequest.getItems()));
                            });
                });
    }

    // --- helpers privados ---

    private BigDecimal calculateTotal(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderDetail> mapToOrderDetails(Long orderId, List<OrderItemDto> items) {
        if (items == null) return List.of();
        return items.stream().map(i -> {
            OrderDetail od = OrderDetail.builder()
                    .orderId(orderId)
                    .productId(i.getProductId())
                    .quantity(i.getQuantity())
                    .unitPrice(i.getUnitPrice())
                    .build();
            return od;
        }).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Long orderId, String customerName, String status,
                                             BigDecimal totalAmount, List<OrderItemDto> items) {
        OrderResponse resp = new OrderResponse();
        resp.setOrderId(orderId);
        resp.setStatus(status);
        resp.setTotalAmount(totalAmount);
        // si tu OrderResponse incluye customerName y items, ajusta:
        // resp.setCustomerName(customerName);
        // resp.setItems(items);
        return resp;
    }
}
