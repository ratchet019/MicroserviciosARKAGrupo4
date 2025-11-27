/*
package com.arka.ecommerce.mssales.controller;

import com.arka.ecommerce.mssales.dto.*;
import com.arka.ecommerce.mssales.service.ReportService;
import com.arka.ecommerce.mssales.service.SaleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SaleController.class)
@SpringBootTest(classes = com.arka.ecommerce.mssales.SalesServiceApplication.class)
@AutoConfigureWebTestClient
class SaleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SaleService saleService;

    @MockBean
    private ReportService reportService;

    @Test
    void createSale_shouldReturnOk() {
        SaleDetailRequest detail = new SaleDetailRequest(10L, 2, BigDecimal.valueOf(15));
        SaleRequest req = new SaleRequest("Thiago", 1L, List.of(detail));
        SaleDto dto = new SaleDto(1L, LocalDateTime.now(), BigDecimal.valueOf(30),
                "Thiago", "COMPLETED", List.of(detail));

        Mockito.when(saleService.registerSale(Mockito.any()))
                .thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/sales")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalAmount").isEqualTo(30);
    }

    @Test
    void weeklyReport_shouldReturnOk() {
        SaleReportDto report = new SaleReportDto(LocalDate.now().minusDays(7), LocalDate.now(),
                BigDecimal.valueOf(1500), 10);

        Mockito.when(reportService.weeklyReport()).thenReturn(Mono.just(report));

        webTestClient.get()
                .uri("/api/v1/sales/report/weekly")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalSales").isEqualTo(1500);
    }

    @Test
    void abandonedCarts_shouldReturnList() {
        List<AbandonedCartDto> carts = List.of(
                new AbandonedCartDto(100L, "Carlos", LocalDateTime.now().minusDays(2), BigDecimal.valueOf(45)),
                new AbandonedCartDto(101L, "Maria", LocalDateTime.now().minusDays(3), BigDecimal.valueOf(60))
        );

        Mockito.when(saleService.findAbandonedCarts()).thenReturn(Mono.just(carts));

        webTestClient.get()
                .uri("/api/v1/sales/abandoned-carts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AbandonedCartDto.class)
                .hasSize(2)
                .value(list -> {
                    Assertions.assertEquals("Carlos", list.get(0).getCustomerName());
                    Assertions.assertEquals("Maria", list.get(1).getCustomerName());
                });
    }
}
*/
