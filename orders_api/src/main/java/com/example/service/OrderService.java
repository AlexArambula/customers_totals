package com.example.service;

import com.example.controller.OrderUpdateCriteria;
import com.example.dto.CustomerTotalsDto;
import com.example.dto.OrderDto;
import com.example.entity.Customer;
import com.example.entity.CustomerTotals;
import com.example.entity.Order;
import com.example.exception.OrderNotFoundException;
import com.example.mapper.CustomerTotalsMapper;
import com.example.mapper.OrderMapper;
import com.example.repository.CustomerTotalsRepository;
import com.example.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@CacheConfig(cacheNames = {"orders-cache", "order-totals-cache"})
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerTotalsRepository customerTotalsRepository;
    private final OrderMapper orderMapper;
    private final CustomerTotalsMapper customerTotalsMapper;

    private final MessagingService messagingService;

    public Mono<OrderDto> createOrder(@NonNull OrderDto order) {
        Order orderEntity = orderMapper.dtoToEntity(order);
        orderEntity.setStatus(Order.Status.CREATED);
        return orderRepository.insert(orderEntity)
                .map(orderMapper::entityToDto)
                .doOnSuccess(orderDto -> messagingService.sendCreateOrderMessage(orderDto.id()));
    }

    public Flux<OrderDto> fetchOrders() {
        return orderRepository.findAll().map(orderMapper::entityToDto);
    }

    @Cacheable("orders-cache")
    public Mono<OrderDto> fetchOrderById(@NonNull String orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)))
                .map(orderMapper::entityToDto)
                .cache();
    }

    public Flux<OrderDto> fetchCustomerOrders(@NonNull String customerId) {
        final Order criteria = Order.builder()
                .customer(new Customer(customerId))
                .build();
        return orderRepository
                .findAll(Example.of(criteria), Sort.by("createdAt"))
                .map(orderMapper::entityToDto);
    }

    public Mono<OrderDto> updateOrder(@NonNull String orderId, OrderUpdateCriteria criteria) {
        if (criteria == OrderUpdateCriteria.CANCEL) {
            return orderRepository.findById(orderId)
                    .switchIfEmpty(Mono.error(new OrderNotFoundException(orderId)))
                    .map(order -> {
                        order.setStatus(Order.Status.CANCELED);
                        return order;
                    })
                    .flatMap(orderRepository::save)
                    .map(orderMapper::entityToDto)
                    .doOnSuccess(orderDto -> messagingService.sendCancelOrderMessage(orderDto.id()));
        }
        return Mono.empty();
    }

    public Mono<CustomerTotalsDto> fetchCustomerTotals(@NonNull String customerId) {
        final CustomerTotals criteria = new CustomerTotals(null, new Customer(customerId), null);
        return customerTotalsRepository.findOne(Example.of(criteria))
                .map(customerTotalsMapper::entityToDto);
    }
}
