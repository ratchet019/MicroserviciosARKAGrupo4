package com.arka.ecommerce.mssales.repository;

import com.arka.ecommerce.mssales.entity.SaleDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SaleDetailRepository extends ReactiveCrudRepository<SaleDetail, Long> {
    Flux<SaleDetail> findBySaleId(Long saleId);
}
