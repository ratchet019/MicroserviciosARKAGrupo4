package com.arka.orderservice.controller;

import com.arka.orderservice.dto.*;
import com.arka.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OrderController controller = new OrderController(orderService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void createOrder_controller_success() {
        OrderItemDto item = new OrderItemDto(1L, 2, BigDecimal.valueOf(5));
        OrderRequest req = new OrderRequest("Luis", List.of(item));
        OrderResponse resp = new OrderResponse(42L, "PENDING", BigDecimal.valueOf(10));

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(Mono.just(resp));

        webTestClient.post()
                .uri("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(42)
                .jsonPath("$.status").isEqualTo("PENDING")
                .jsonPath("$.totalAmount").isEqualTo(10);

        verify(orderService, times(1)).createOrder(any(OrderRequest.class));
    }

    @Test
    void confirmOrder_controller_success() {
        Long id = 5L;
        OrderResponse resp = new OrderResponse(id, "CONFIRMED", BigDecimal.valueOf(100));
        when(orderService.confirmOrder(id)).thenReturn(Mono.just(resp));

        webTestClient.put()
                .uri("/api/v1/orders/{id}/confirm", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(5)
                .jsonPath("$.status").isEqualTo("CONFIRMED");

        verify(orderService, times(1)).confirmOrder(id);
    }

    @Test
    void getOrder_controller_success() {
        Long id = 7L;
        OrderResponse resp = new OrderResponse(id, "PENDING", BigDecimal.valueOf(20));
        when(orderService.getOrderSummary(id)).thenReturn(Mono.just(resp));

        webTestClient.get()
                .uri("/api/v1/orders/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(7)
                .jsonPath("$.status").isEqualTo("PENDING");

        verify(orderService, times(1)).getOrderSummary(id);
    }
}
