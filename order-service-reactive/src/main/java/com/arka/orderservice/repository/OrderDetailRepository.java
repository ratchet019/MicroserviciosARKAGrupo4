package com.arka.orderservice.repository;

import com.arka.orderservice.model.OrderDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderDetailRepository extends ReactiveCrudRepository<OrderDetail, Long> {
    Flux<OrderDetail> findByOrderId(Long orderId);

    @Query("DELETE FROM order_detail WHERE order_id = :orderId")
    Mono<Void> deleteByOrderId(Long orderId);
}
