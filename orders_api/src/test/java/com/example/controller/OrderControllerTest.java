package com.example.controller;

import com.example.dto.OrderDto;
import com.example.exception.OrderNotFoundException;
import com.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.example.mocks.CustomerMock.mockCustomer;
import static com.example.mocks.ItemMock.mockItem;
import static com.example.mocks.OrderMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private final static String BASE_PATH = "/orders";
    private final static String MOCK_ORDER_PATH = BASE_PATH + "/" + id;

    private WebTestClient webTestClient;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(new OrderController(orderService))
                .build();
    }

    @Test
    void create() {
        final OrderDto orderDto = new OrderDto(null, mockCustomer, List.of(mockItem), createdAt, lastModifiedAt, null);
        doAnswer(invocation ->
                Mono.just(mockOrder))
                .when(orderService).createOrder(eq(orderDto));

        webTestClient.post()
                .uri(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(orderDto), OrderDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrderDto.class).consumeWith(result -> {
                    final OrderDto order = result.getResponseBody();
                    assertNotNull(order);
                    assertEquals(id, order.id());
                    assertEquals(mockCustomer, order.customer());
                    assertEquals(createdAt, order.createdAt());
                    assertEquals(lastModifiedAt, order.lastModifiedAt());
                    assertEquals(OrderDto.Status.CREATED, order.status());
                    assertThat(order.items()).hasSize(1)
                            .containsExactly(mockItem);
                });
    }

    @Test
    void fetchAll() {
        webTestClient.get()
                .uri(BASE_PATH)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchById() {
        doAnswer(invocation ->
            Mono.just(mockOrder))
            .when(orderService).fetchOrderById(eq(id));

        webTestClient.get()
                .uri(MOCK_ORDER_PATH)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fetchById_testNotFound() {
        doAnswer(invocation ->
                Mono.error(new OrderNotFoundException()))
                .when(orderService).fetchOrderById(anyString());

        webTestClient.get()
                .uri(MOCK_ORDER_PATH)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void update() {
        doAnswer(invocation ->
                Mono.just(mockOrder))
                .when(orderService).updateOrder(eq(id), eq(OrderUpdateCriteria.CANCEL));

        webTestClient.put()
                .uri(getUriWithParameters(MOCK_ORDER_PATH, Map.of("operation", "CANCEL")))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void update_testNotFound() {
        doAnswer(invocation ->
                Mono.error(new OrderNotFoundException()))
                .when(orderService).updateOrder(eq(id), eq(OrderUpdateCriteria.CANCEL));

        webTestClient.put()
                .uri(getUriWithParameters(MOCK_ORDER_PATH, Map.of("operation", "CANCEL")))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void update_testBadRequest() {
        webTestClient.put()
                .uri(MOCK_ORDER_PATH)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private URI getUriWithParameters(String uri, Map<String, String> params) {
        var builder = UriComponentsBuilder.fromUriString(uri);
        for (Map.Entry<String, String> entry: params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build().toUri();
    }
}