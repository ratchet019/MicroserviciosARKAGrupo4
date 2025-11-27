package com.arka.ecommerce.msmovement.repository;

import com.arka.ecommerce.msmovement.entity.Movements;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovementRepository extends ReactiveCrudRepository<Movements, Long> {
    Flux<Movements> findByProductId(Long productId);
}
