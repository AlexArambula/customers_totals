package com.example.service;

import com.example.entity.Customer;
import com.example.entity.CustomerTotals;
import com.example.entity.Item;
import com.example.entity.Order;
import com.example.mocks.CustomerTotalsMock;
import com.example.mocks.OrderMock;
import com.example.repository.CustomerTotalsRepository;
import com.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static com.example.mocks.OrderMock.mockOrder;
import static com.example.mocks.OrderMock.mockOrder2Items;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerTotalsRepository customerTotalsRepository;

    @Test
    void updateTotalsWithNewOrder_testNewTotals() {
        assertEquals(1, mockOrder.getItems().size());
        final Item orderItem = mockOrder.getItems().get(0);
        final CustomerTotals expectedTotals = new CustomerTotals(null, mockOrder.getCustomer(),
                Map.of(orderItem.category(), new CustomerTotals.CategoryTotal(1L, orderItem.price())));

        final Example<CustomerTotals> criteria =
                Example.of(new CustomerTotals(new Customer(mockOrder.getCustomer().id())));
        final Mono<CustomerTotals> totalsPub = Mono.just(expectedTotals);
        doReturn(Mono.empty()).when(customerTotalsRepository).findOne(eq(criteria));
        doReturn(totalsPub).when(customerTotalsRepository).save(eq(expectedTotals));

        final Mono<Order> orderPub = Mono.just(mockOrder);
        doReturn(orderPub).when(orderRepository).findById(eq(mockOrder.getId()));

        StepVerifier.create(totalsPub).expectNext(expectedTotals).verifyComplete();
        StepVerifier.create(orderPub).expectNext(mockOrder).verifyComplete();

        orderService.updateTotalsWithNewOrder(OrderMock.id);
        Mockito.verify(customerTotalsRepository, times(1)).save(eq(expectedTotals));
    }

    @Test
    void updateTotalsWithNewOrder_testUpdatedTotals() {
        assertEquals(1, mockOrder.getItems().size());
        final Item orderItem = mockOrder.getItems().get(0);

        final Map<String, CustomerTotals.CategoryTotal> categoryTotals = new HashMap<>();
        categoryTotals.put(orderItem.category(), new CustomerTotals.CategoryTotal(1L, orderItem.price()));
        final CustomerTotals currentTotals = new CustomerTotals(CustomerTotalsMock.id, mockOrder.getCustomer(), categoryTotals);

        final Map<String, CustomerTotals.CategoryTotal> categoryTotalsNew = new HashMap<>();
        categoryTotalsNew.put(orderItem.category(), new CustomerTotals.CategoryTotal(2L, orderItem.price() * 2));
        final CustomerTotals expectedTotals = new CustomerTotals(CustomerTotalsMock.id, mockOrder.getCustomer(), categoryTotalsNew);

        final Example<CustomerTotals> criteria =
                Example.of(new CustomerTotals(new Customer(mockOrder.getCustomer().id())));
        final Mono<CustomerTotals> totalsPub = Mono.just(expectedTotals);
        doReturn(Mono.just(currentTotals)).when(customerTotalsRepository).findOne(eq(criteria));
        doReturn(totalsPub).when(customerTotalsRepository).save(eq(currentTotals));

        final Mono<Order> orderPub = Mono.just(mockOrder);
        doReturn(orderPub).when(orderRepository).findById(eq(mockOrder.getId()));

        StepVerifier.create(totalsPub).expectNext(expectedTotals).verifyComplete();
        StepVerifier.create(orderPub).expectNext(mockOrder).verifyComplete();

        orderService.updateTotalsWithNewOrder(OrderMock.id);
        Mockito.verify(customerTotalsRepository, times(1)).save(eq(currentTotals));
    }

    @Test
    void updateTotalsWithCanceledOrder() {
        assertEquals(2, mockOrder2Items.getItems().size());
        final Item item1 = mockOrder2Items.getItems().get(0);
        final Item item2 = mockOrder2Items.getItems().get(1);

        final Map<String, CustomerTotals.CategoryTotal> categoryTotals = new HashMap<>();
        categoryTotals.put(item1.category(), new CustomerTotals.CategoryTotal(1L, item1.price()));
        categoryTotals.put(item2.category(), new CustomerTotals.CategoryTotal(1L, item2.price()));
        final CustomerTotals currentTotals = new CustomerTotals(CustomerTotalsMock.id, mockOrder2Items.getCustomer(), categoryTotals);

        final Map<String, CustomerTotals.CategoryTotal> categoryTotalsNew = new HashMap<>();
        categoryTotalsNew.put(item2.category(), new CustomerTotals.CategoryTotal(1L, item2.price()));
        final CustomerTotals expectedTotals = new CustomerTotals(CustomerTotalsMock.id, mockOrder2Items.getCustomer(), categoryTotalsNew);

        final Example<CustomerTotals> criteria =
                Example.of(new CustomerTotals(new Customer(mockOrder.getCustomer().id())));
        final Mono<CustomerTotals> totalsPub = Mono.just(expectedTotals);
        doReturn(Mono.just(currentTotals)).when(customerTotalsRepository).findOne(eq(criteria));
        doReturn(totalsPub).when(customerTotalsRepository).save(eq(currentTotals));

        mockOrder.setStatus(Order.Status.CANCELED);
        final Mono<Order> orderPub = Mono.just(mockOrder);
        doReturn(orderPub).when(orderRepository).findById(eq(mockOrder.getId()));

        StepVerifier.create(totalsPub).expectNext(expectedTotals).verifyComplete();
        StepVerifier.create(orderPub).expectNext(mockOrder).verifyComplete();

        orderService.updateTotalsWithCanceledOrder(OrderMock.id);
        Mockito.verify(customerTotalsRepository, times(1)).save(eq(expectedTotals));
    }
}