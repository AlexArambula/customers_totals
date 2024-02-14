package com.example.service;

import com.example.entity.Customer;
import com.example.entity.CustomerTotals;
import com.example.entity.Item;
import com.example.entity.Order;
import com.example.repository.CustomerTotalsRepository;
import com.example.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerTotalsRepository customerTotalsRepository;

    /**
     * Updates the customer totals adding the data from the newly created order.
     * @param orderId
     */
    public void updateTotalsWithNewOrder(@NonNull String orderId) {
        updateTotals(orderId, false).subscribe();
    }

    /**
     * Updates the customer totals removing the data from the newly canceled order.
     * @param orderId
     */
    public void updateTotalsWithCanceledOrder(@NonNull String orderId) {
        updateTotals(orderId, true).subscribe();
    }

    /**
     * Fetches the Order identified by the orderId param and updates the existing totals with the data from the
     * incoming order or creates a new totals in case this is the first customer order.
     * @param orderId The order id.
     * @param isCancel is this a cancel operation?
     * @return Mono<CustomerTotals>
     */
    protected Mono<CustomerTotals> updateTotals(String orderId, boolean isCancel) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    final CustomerTotals criteria = new CustomerTotals(new Customer(order.getCustomer().id()));
                    return customerTotalsRepository.findOne(Example.of(criteria))
                            .map(customerTotals -> mergeTotals(customerTotals, order, isCancel))
                            .defaultIfEmpty(
                                    new CustomerTotals(null, order.getCustomer(), getTotalsFromOrder(order)))
                            .filter(customerTotals -> customerTotals.id() != null || !isCancel);
                })
                .flatMap(customerTotalsRepository::save);
    }

    /**
     * Merges the Order's items totals into a given CustomerTotals data.
     * @param customerTotals
     * @param order
     * @param isCancel
     * @return
     */
    private CustomerTotals mergeTotals(CustomerTotals customerTotals, Order order, boolean isCancel) {
        var totals = customerTotals.totals();
        for (Item item: order.getItems()) {
            Long qty = isCancel? -1L : 1L;
            Double total = isCancel? -item.price() : item.price();
            if (totals.containsKey(item.category())) {
                qty += totals.get(item.category()).quantity();
                total += totals.get(item.category()).amount();
            }
            if (qty == 0) {
                totals.remove(item.category());
            } else {
                totals.put(item.category(), new CustomerTotals.CategoryTotal(qty, total));
            }
        }
        return new CustomerTotals(customerTotals.id(), customerTotals.customer(), totals);
    }

    /**
     * Creates a <code>java.util.Map</code> using the item's category as a key and the totals as value
     * from a given order's items.
     * @param order
     * @return
     */
    private Map<String, CustomerTotals.CategoryTotal> getTotalsFromOrder(Order order) {
        Map<String, CustomerTotals.CategoryTotal> totals = new HashMap<>();
        for (Item item: order.getItems()) {
            Long qty = 1L;
            Double total = item.price();
            if (totals.containsKey(item.category())) {
                qty += totals.get(item.category()).quantity();
                total += totals.get(item.category()).amount();
            }
            totals.put(item.category(), new CustomerTotals.CategoryTotal(qty, total));
        }
        return totals;
    }
}
