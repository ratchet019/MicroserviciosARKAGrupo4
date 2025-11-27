package com.arka.ecommerce.msmovement.service;


import com.arka.ecommerce.msmovement.dto.MovementRequest;
import com.arka.ecommerce.msmovement.entity.Movements;
import com.arka.ecommerce.msmovement.repository.MovementRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

public class MovementServiceTest {

    @Mock
    private MovementRepository repository;
    @Mock
    private WebClient webClient;
    @InjectMocks
    private MovementService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterInMovement() {
        MovementRequest req = new MovementRequest(1L, "IN", 10, "COMPRA-01");
        Movements saved = new Movements(1L, 1L, "IN", 10, "COMPRA-01", LocalDateTime.now());

        when(repository.save(any())).thenReturn(Mono.just(saved));
        when(webClient.post()).thenReturn(mock(WebClient.RequestBodyUriSpec.class));

        StepVerifier.create(service.registerMovement(req))
                .expectNextMatches(m -> m.getMovementType().equals("IN"))
                .verifyComplete();
    }


}

