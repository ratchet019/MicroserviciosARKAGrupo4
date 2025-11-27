package com.arka.ecommerce.msmovement.controller;

import com.arka.ecommerce.msmovement.dto.MovementDto;
import com.arka.ecommerce.msmovement.dto.MovementRequest;
import com.arka.ecommerce.msmovement.service.MovementService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

@WebFluxTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovementService movementService;

    @Test
    void shouldReturnOkWhenRequestIsValid() {
        MovementRequest request = new MovementRequest(1L, "IN", 10, "COMPRA-01");
        MovementDto dto = new MovementDto(1L, 1L, "IN", 10, "COMPRA-01", LocalDateTime.now());

        when(movementService.registerMovement(request)).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri("/api/v1/movements")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.movementType").isEqualTo("IN");
    }

    @Test
    void shouldReturnBadRequestWhenMovementTypeIsBlank() {
        MovementRequest request = new MovementRequest(1L, "", 10, "COMPRA-01");

        webTestClient.post()
                .uri("/api/v1/movements")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Error de validación")
                .jsonPath("$.message").isEqualTo("Datos inválidos en la solicitud")
                .jsonPath("$.path").isEqualTo("/api/v1/movements")
                .jsonPath("$.details[0]").value(containsString("movementType"));

    }

  /*  @Test
    void shouldReturnInsufficientStockWhenDeltaIsGreaterThanAvailable() {
        MovementRequest request = new MovementRequest(1L, "SALE", 1000, "VENTA-01");

        webTestClient.post()
                .uri("/api/v1/movements")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Error de validación")
                .jsonPath("$.message").isEqualTo("Stock insuficiente para realizar la operación")
                .jsonPath("$.path").isEqualTo("/api/v1/movements")
                .jsonPath("$.details[0]").value(containsString("InsufficientStockException"));
    }*/
}
