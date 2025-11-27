/*package com.arka.ecommerce.mssales.service;

import com.arka.ecommerce.mssales.dto.SaleDetailRequest;
import com.arka.ecommerce.mssales.dto.SaleRequest;
import com.arka.ecommerce.mssales.entity.SaleHeader;
import com.arka.ecommerce.mssales.repository.SaleDetailRepository;
import com.arka.ecommerce.mssales.repository.SaleHeaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private SaleHeaderRepository saleHeaderRepository;

    @Mock
    private SaleDetailRepository saleDetailRepository;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setup() {
        // mocks del flujo WebClient
        Mockito.lenient().when(webClient.get())
                .thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);

        Mockito.lenient().when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.any(Object[].class)))
                .thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);

        Mockito.lenient().when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        Mockito.lenient().when(responseSpec.bodyToMono(Mockito.eq(Integer.class)))
                .thenReturn(Mono.just(10)); // stock simulado
    }

    @Test
    void registerSale_shouldCreateSaleSuccessfully() {
        SaleDetailRequest detail = new SaleDetailRequest(1L, 2, BigDecimal.valueOf(10));
        SaleRequest req = new SaleRequest("Thiago", 1L, List.of(detail));

        SaleHeader savedHeader = new SaleHeader();
        savedHeader.setId(1L);
        savedHeader.setCustomerName("Thiago");
        savedHeader.setSaleDate(LocalDateTime.now());
        savedHeader.setTotalAmount(BigDecimal.valueOf(20));
        savedHeader.setStatus("COMPLETED");

        Mockito.when(saleHeaderRepository.save(Mockito.any(SaleHeader.class)))
                .thenReturn(Mono.just(savedHeader));

        // suponiendo saleService.registerSale devuelve Mono<SaleDto> (ajustá según tu implementación)
        // aquí mapeo el SaleDto a SaleHeader solo para la verificación; en tu proyecto adapta el tipo correcto
        Mono<SaleHeader> result = saleService.registerSale(req)
                .map(dto -> {
                    SaleHeader h = new SaleHeader();
                    h.setCustomerName(dto.getCustomerName());
                    h.setTotalAmount(dto.getTotalAmount());
                    return h;
                });

        StepVerifier.create(result)
                .expectNextMatches(h ->
                        h.getCustomerName().equals("Thiago") &&
                                h.getTotalAmount().equals(BigDecimal.valueOf(20)))
                .verifyComplete();
    }
}
*/