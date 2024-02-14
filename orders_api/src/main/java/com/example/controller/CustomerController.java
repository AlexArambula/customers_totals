package com.example.controller;

import com.example.dto.CustomerDto;
import com.example.dto.CustomerTotalsDto;
import com.example.dto.OrderDto;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDto> create(@RequestBody @Valid CustomerDto customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CustomerDto> fetchAll() {
        return customerService.fetchCustomers();
    }

    @GetMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.OK)
    public Flux<OrderDto> fetchCustomerOrders(@PathVariable String id) {
        return orderService.fetchCustomerOrders(id);
    }

    @GetMapping("/{id}/totals")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerTotalsDto> fetchCustomerTotals(@PathVariable String id) {
        return orderService.fetchCustomerTotals(id);
    }
}
