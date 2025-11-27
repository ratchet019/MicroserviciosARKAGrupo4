package com.arka.orderservice.service;

import com.arka.orderservice.dto.*;
import com.arka.orderservice.model.*;
import com.arka.orderservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository detailRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, detailRepository);
    }

    @Test
    void createOrder_success() {
        OrderItemDto item = new OrderItemDto(1L, 2, BigDecimal.valueOf(10));
        OrderRequest req = new OrderRequest("Juan", List.of(item));

        Order savedOrder = Order.builder()
                .id(100L)
                .orderDate(LocalDateTime.now())
                .customerName("Juan")
                .status("PENDING")
                .totalAmount(BigDecimal.valueOf(20))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(savedOrder));
        when(detailRepository.saveAll(any(Flux.class)))
                .thenAnswer(invocation -> {
                    Flux<OrderDetail> flux = invocation.getArgument(0);
                    return flux.map(d -> {
                        d.setId(10L);
                        return d;
                    });
                });

        Mono<OrderResponse> result = orderService.createOrder(req);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.getOrderId().equals(100L)
                        && "PENDING".equals(resp.getStatus())
                        && resp.getTotalAmount().compareTo(BigDecimal.valueOf(20)) == 0)
                .verifyComplete();

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(detailRepository, times(1)).saveAll(any(Flux.class));
    }

    @Test
    void confirmOrder_success() {
        Long id = 200L;
        Order existing = Order.builder()
                .id(id)
                .orderDate(LocalDateTime.now())
                .customerName("Ana")
                .status("PENDING")
                .totalAmount(BigDecimal.valueOf(50))
                .build();

        when(orderRepository.findById(id)).thenReturn(Mono.just(existing));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<OrderResponse> result = orderService.confirmOrder(id);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.getOrderId().equals(id)
                        && "CONFIRMED".equals(resp.getStatus()))
                .verifyComplete();

        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void confirmOrder_notFound() {
        Long id = 999L;
        when(orderRepository.findById(id)).thenReturn(Mono.empty());

        Mono<OrderResponse> result = orderService.confirmOrder(id);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException
                        && throwable.getMessage().contains("Order not found"))
                .verify();

        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void confirmOrder_alreadyProcessed() {
        Long id = 300L;
        Order existing = Order.builder()
                .id(id)
                .orderDate(LocalDateTime.now())
                .customerName("Carlos")
                .status("CONFIRMED")
                .totalAmount(BigDecimal.valueOf(30))
                .build();

        when(orderRepository.findById(id)).thenReturn(Mono.just(existing));

        Mono<OrderResponse> result = orderService.confirmOrder(id);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException
                        && throwable.getMessage().contains("Order already processed"))
                .verify();

        verify(orderRepository, times(1)).findById(id);
    }
}
