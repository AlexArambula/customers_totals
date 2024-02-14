package com.example.controller;

import com.example.dto.OrderDto;
import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderDto> create(@RequestBody OrderDto order) {
        return orderService.createOrder(order);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderDto> fetchAll() {
        return orderService.fetchOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<OrderDto>> fetchById(@PathVariable String id) {
        return orderService.fetchOrderById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .map(optional -> optional.map(ResponseEntity::ok).orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderDto>> update(@PathVariable String id,
                                 @RequestParam(name = "operation") OrderUpdateCriteria criteria) {
        return orderService.updateOrder(id, criteria)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .map(optional -> optional.map(ResponseEntity::ok).orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
