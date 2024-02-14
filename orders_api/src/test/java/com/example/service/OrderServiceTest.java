package com.example.service;

import com.example.controller.OrderUpdateCriteria;
import com.example.dto.OrderDto;
import com.example.entity.Customer;
import com.example.entity.CustomerTotals;
import com.example.entity.Order;
import com.example.mapper.CustomerTotalsMapper;
import com.example.mapper.OrderMapper;
import com.example.repository.CustomerTotalsRepository;
import com.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.example.mocks.CustomerMock.mockCustomer;
import static com.example.mocks.CustomerTotalsMock.mockCustomerTotals;
import static com.example.mocks.ItemMock.mockItem;
import static com.example.mocks.OrderMock.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerTotalsRepository customerTotalsRepository;
    @Mock
    private MessagingService messagingService;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Spy
    private CustomerTotalsMapper customerTotalsMapper = Mappers.getMapper(CustomerTotalsMapper.class);

    @Test
    void createOrder() {
        final OrderDto orderDto = new OrderDto(null, mockCustomer, List.of(mockItem), createdAt, lastModifiedAt,
                OrderDto.Status.CREATED);
        final Order order = orderMapper.dtoToEntity(orderDto);
        doAnswer(invocation ->
                Mono.just(orderMapper.dtoToEntity(mockOrder)))
                .when(orderRepository).insert(eq(order));

        StepVerifier.create(orderService.createOrder(orderDto))
                .expectNext(mockOrder)
                .verifyComplete();
        Mockito.verify(orderRepository, times(1)).insert(eq(order));
        Mockito.verify(messagingService, times(1)).sendCreateOrderMessage(eq(mockOrder.id()));
    }

    @Test
    void deleteOrderById() {
        orderService.deleteOrderById(id);
        Mockito.verify(orderRepository, times(1)).deleteById(eq(id));
    }

    @Test
    void fetchOrders() {
        doAnswer(invocation ->
                Flux.just(orderMapper.dtoToEntity(mockOrder)))
                .when(orderRepository).findAll();

        orderService.fetchOrders();
        Mockito.verify(orderRepository, times(1)).findAll();
    }

    @Test
    void fetchOrderById() {
        doAnswer(invocation ->
                Mono.just(orderMapper.dtoToEntity(mockOrder)))
                .when(orderRepository).findById(eq(id));

        orderService.fetchOrderById(id);
        Mockito.verify(orderRepository, times(1)).findById(eq(id));
    }

    @Test
    void fetchCustomerOrders() {
        final Example<Order> criteria = Example.of(Order.builder()
                .customer(new Customer(mockCustomer.id()))
                .build());
        final Sort sortCriteria = Sort.by("createdAt");
        doAnswer(invocation ->
                Flux.just(orderMapper.dtoToEntity(mockOrder)))
                .when(orderRepository).findAll(eq(criteria), eq(sortCriteria));

        orderService.fetchCustomerOrders(mockCustomer.id());
        Mockito.verify(orderRepository, times(1)).findAll(eq(criteria), eq(sortCriteria));
    }

    @Test
    void updateOrder() {
        doAnswer(invocation ->
                Mono.just(orderMapper.dtoToEntity(mockOrder)))
                .when(orderRepository).findById(eq(id));

        final OrderDto canceledOrderDto = new OrderDto(mockOrder.id(), mockOrder.customer(), mockOrder.items(),
                mockOrder.createdAt(), mockOrder.lastModifiedAt(), OrderDto.Status.CANCELED);
        final Order canceledOrder = orderMapper.dtoToEntity(canceledOrderDto);
        doAnswer(invocation ->
                Mono.just(canceledOrder))
                .when(orderRepository).save(canceledOrder);

        StepVerifier.create(orderService.updateOrder(id, OrderUpdateCriteria.CANCEL))
                .expectNext(canceledOrderDto)
                .verifyComplete();

        Mockito.verify(orderRepository, times(1)).findById(eq(id));
        Mockito.verify(orderRepository, times(1)).save(eq(canceledOrder));
        Mockito.verify(messagingService, times(1)).sendCancelOrderMessage(eq(canceledOrder.getId()));
    }

    @Test
    void fetchCustomerTotals() {
        final Example<CustomerTotals> criteria = Example.of(new CustomerTotals(null, new Customer(mockCustomer.id()), null));
        doAnswer(invocation ->
                Mono.just(mockCustomerTotals))
                .when(customerTotalsRepository).findOne(criteria);

        orderService.fetchCustomerTotals(mockCustomer.id());
        Mockito.verify(customerTotalsRepository, times(1)).findOne(criteria);
    }
}