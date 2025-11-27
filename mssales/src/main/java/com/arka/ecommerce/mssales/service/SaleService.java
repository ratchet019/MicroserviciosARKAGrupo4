package com.arka.ecommerce.mssales.service;


import com.arka.ecommerce.mssales.dto.*;
import com.arka.ecommerce.mssales.entity.SaleDetail;
import com.arka.ecommerce.mssales.entity.SaleHeader;
import com.arka.ecommerce.mssales.exception.OutOfStockException;
import com.arka.ecommerce.mssales.repository.SaleDetailRepository;
import com.arka.ecommerce.mssales.repository.SaleHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;




@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleHeaderRepository headerRepo;
    private final SaleDetailRepository detailRepo;

    private final WebClient inventoryWebClient;
    private final WebClient movementWebClient;
    private final WebClient orderWebClient;

    public Mono<SaleDto> registerSale(SaleRequest request) {
        return Flux.fromIterable(request.getDetails())
                .flatMap(detail -> validateStock(detail)
                        .then(Mono.just(detail)))
                .collectList()
                .flatMap(validDetails -> {
                    BigDecimal total = validDetails.stream()
                            .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    SaleHeader header = new SaleHeader(null, LocalDateTime.now(), total,
                            request.getCustomerName(), request.getOrderId(), "COMPLETED");

                    return headerRepo.save(header)
                            .flatMap(savedHeader -> Flux.fromIterable(validDetails)
                                    .flatMap(d -> {
                                        SaleDetail sd = new SaleDetail(null, savedHeader.getId(),
                                                d.getProductId(), d.getQuantity(), d.getUnitPrice(),
                                                d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())));
                                        return detailRepo.save(sd)
                                                .then(discountStock(d))
                                                .then(Mono.just(sd));
                                    })
                                    .collectList()
                                    .map(savedDetails -> new SaleDto(savedHeader.getId(),
                                            savedHeader.getSaleDate(), savedHeader.getTotalAmount(),
                                            savedHeader.getCustomerName(), savedHeader.getStatus(), validDetails))
                            );
                });
    }

    private Mono<Void> validateStock(SaleDetailRequest detail) {
        return inventoryWebClient.get()
                .uri("/api/v1/inventory/{id}/stock", detail.getProductId())
                .retrieve()
                .bodyToMono(Integer.class)
                .flatMap(stock -> stock >= detail.getQuantity() ?
                        Mono.empty() : Mono.error(new OutOfStockException(detail.getProductId())));
    }


    private Mono<Void> discountStock(SaleDetailRequest detail) {
        return movementWebClient.post()
                .uri("/api/v1/movements")
                .bodyValue(Map.of(
                        "productId", detail.getProductId(),
                        "movementType", "OUT", //  obligatorio
                        "delta", -detail.getQuantity(), //  obligatorio (negativo para venta)
                        "reference", "SALE" // opcional
                ))
                .retrieve()
                .bodyToMono(Void.class);
    }


    public Mono<List<AbandonedCartDto>> findAbandonedCarts() {
        return orderWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/order/pending")
                        .queryParam("olderThanHours", 24)
                        .build())
                .retrieve()
                .bodyToFlux(AbandonedCartDto.class)
                .collectList();
    }


}


