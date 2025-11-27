package com.arka.ecommerce.msinventory.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.arka.ecommerce.msinventory.dto.InventoryDto;
import com.arka.ecommerce.msinventory.model.Inventory;

import reactor.core.publisher.Mono;


public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Long>{

    Mono<Inventory> findBySkuCode(String skuCode);

    Mono<InventoryDto> findByProductId(Long productId);


}
