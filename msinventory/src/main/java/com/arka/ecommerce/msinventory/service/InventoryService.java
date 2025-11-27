package com.arka.ecommerce.msinventory.service;

import org.springframework.stereotype.Service;

import com.arka.ecommerce.msinventory.dto.InventoryDto;
import com.arka.ecommerce.msinventory.repository.InventoryRepository;

import reactor.core.publisher.Mono;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Mono<InventoryDto> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> new InventoryDto(inventory.getProductId(), inventory.getQuantity(), inventory.getSkuCode()));
    }

    public Mono<InventoryDto> getInventoryBySkuCode(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> new InventoryDto(inventory.getProductId(), inventory.getQuantity(), inventory.getSkuCode()));
    }

    public Mono<Void> updateInventory(String skuCode, int quantity) {
        return inventoryRepository.findBySkuCode(skuCode)
                .flatMap(inventory -> {
                    inventory.setQuantity(quantity);
                    return inventoryRepository.save(inventory);
                })
                .then();
    }

    public Mono<Void> addInventory(String skuCode, int quantity) {
        return inventoryRepository.findBySkuCode(skuCode)
                .flatMap(inventory -> {
                    inventory.setQuantity(inventory.getQuantity() + quantity);
                    return inventoryRepository.save(inventory);
                })
                .then();
    }

    public Mono<Void> deleteInventory(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .flatMap(inventory -> inventoryRepository.delete(inventory));
    }

}
