package com.example.listener;

import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final OrderService orderService;

    @RabbitListener(queues = {"${messaging.queue.order.create}"})
    public void onCreateOrder(String orderId) {
        orderService.updateTotalsWithNewOrder(orderId);
    }

    @RabbitListener(queues = {"${messaging.queue.order.cancel}"})
    public void onCancelOrder(String orderId) {
        orderService.updateTotalsWithCanceledOrder(orderId);
    }
}
