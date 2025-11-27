package com.arka.ecommerce.mssales.repository;

import com.arka.ecommerce.mssales.entity.SaleHeader;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface SaleHeaderRepository extends ReactiveCrudRepository<SaleHeader, Long> {
    Flux<SaleHeader> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);
}
