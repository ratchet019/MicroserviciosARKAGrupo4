package com.arka.ecommerce.mssales.scheduler;
/*
import com.arka.ecommerce.mssales.dto.AbandonedCartDto;
import com.arka.ecommerce.mssales.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbandonedCartScheduler {

    private final SaleService saleService;

    /**
     * Ejecuta cada 24 horas (86400000 ms)
     */
/*
    @Scheduled(fixedRate = 86_400_000)
    public void checkAbandonedCarts() {
        log.info("🕒 [Scheduler] Iniciando verificación de carritos abandonados: {}", LocalDateTime.now());

        saleService.findAbandonedCarts()
                .flatMapMany(Flux::fromIterable)
                .collectList()
                .flatMap(this::processAbandonedCarts)
                .subscribe(
                        success -> log.info("✅ [Scheduler] Proceso finalizado. {} carritos abandonados detectados.", success.size()),
                        error -> log.error("❌ [Scheduler] Error procesando carritos abandonados", error)
                );
    }

    private Mono<List<AbandonedCartDto>> processAbandonedCarts(List<AbandonedCartDto> carts) {
        if (carts.isEmpty()) {
            log.info("📭 No se encontraron carritos abandonados.");
            return Mono.just(carts);
        }

        carts.forEach(cart ->
                log.warn("⚠️ Carrito abandonado detectado -> Pedido ID: {}, Cliente: {}, Fecha: {}, Total: {}",
                        cart.getOrderId(), cart.getCustomerName(), cart.getOrderDate(), cart.getTotalAmount())
        );

        // Aquí podrías:
        // 1️⃣ Guardarlos en una tabla "abandoned_carts"
        // 2️⃣ O enviar evento al notification-service
        return Mono.just(carts);
    }
}

 */
