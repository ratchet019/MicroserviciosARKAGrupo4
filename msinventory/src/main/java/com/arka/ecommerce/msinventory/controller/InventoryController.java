package com.arka.ecommerce.msinventory.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arka.ecommerce.msinventory.dto.InventoryDto;
import com.arka.ecommerce.msinventory.dto.InventoryInitRequest;
import com.arka.ecommerce.msinventory.service.InventoryService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public Mono<InventoryDto> createInventory(InventoryInitRequest inventoryInitRequest) {
        return inventoryService.addInventory(inventoryInitRequest.getSkuCode(), inventoryInitRequest.getInitialQty())
                .then(Mono.just(new InventoryDto(inventoryInitRequest.getProductId(), inventoryInitRequest.getInitialQty(), inventoryInitRequest.getSkuCode())));
    }

    @GetMapping("/product/{productId}")
    public Mono<InventoryDto> getInventoryByProductId(Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @GetMapping("/sku/{skuCode}")
    public Mono<InventoryDto> getInventoryBySkuCode(String skuCode) {
        return inventoryService.getInventoryBySkuCode(skuCode);
    }

    @GetMapping("/sku/{skuCode}/quantity")
    public Mono<Integer> getInventoryQuantityBySkuCode(String skuCode) {
        return inventoryService.getInventoryBySkuCode(skuCode)
                .map(InventoryDto::getQuantity)
                .defaultIfEmpty(0);
    }

    @PutMapping("/sku/{skuCode}/update")
    public Mono<Void> updateInventory(String skuCode, int quantity) {
        return inventoryService.updateInventory(skuCode, quantity);
    }

    @DeleteMapping("/sku/{skuCode}/delete")
    public Mono<Void> deleteInventory(String skuCode) {
        return inventoryService.deleteInventory(skuCode);
    }

}