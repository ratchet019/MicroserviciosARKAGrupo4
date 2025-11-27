package com.arka.orderservice.repository;

import com.arka.orderservice.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Mono<Order> findById(Long id);
}
