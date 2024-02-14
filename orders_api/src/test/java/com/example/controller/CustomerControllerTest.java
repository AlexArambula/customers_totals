package com.example.controller;

import com.example.dto.CustomerDto;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.example.mocks.CustomerMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private final static String BASE_PATH = "/customers";
    private final static String MOCK_CUSTOMER_PATH = BASE_PATH + "/" + id;

    private WebTestClient webTestClient;

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(new CustomerController(customerService, orderService))
                .build();
    }

    @Test
    void create() {
        final CustomerDto customerDto = new CustomerDto(null, name, email);
        doAnswer(invocation ->
                Mono.just(mockCustomer))
                .when(customerService).createCustomer(eq(customerDto));

        webTestClient.post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerDto), CustomerDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerDto.class).consumeWith(result -> {
                    final CustomerDto customer = result.getResponseBody();
                    assertNotNull(customer);
                    assertEquals(id, customer.id());
                    assertEquals(name, customer.name());
                    assertEquals(email, customer.email());
                });
    }

    @Test
    void create_testWithBadCustomerName() {
        CustomerDto customerDto = new CustomerDto(null, "", email);
        webTestClient.post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerDto), CustomerDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void create_testWithBadCustomerEmail() {
        CustomerDto customerDto = new CustomerDto(null, name, "invalid_email");
        webTestClient.post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerDto), CustomerDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetchAll() {
        webTestClient.get()
                .uri(BASE_PATH)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchCustomerOrders() {
        webTestClient.get()
                .uri(MOCK_CUSTOMER_PATH + "/orders")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchCustomerTotals() {
        webTestClient.get()
                .uri(MOCK_CUSTOMER_PATH + "/totals")
                .exchange()
                .expectStatus().isOk();
    }
}