package com.example.service;

import com.example.dto.OrderDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    @Value("${messaging.queue.order.create}")
    private String createOrderQueue;
    @Value("${messaging.queue.order.cancel}")
    private String cancelOrderQueue;

    private final RabbitTemplate rabbitTemplate;

    public void sendCreateOrderMessage(@NonNull String orderId) {
        rabbitTemplate.convertAndSend(createOrderQueue, orderId);
    }

    public void sendCancelOrderMessage(@NonNull String orderId) {
        rabbitTemplate.convertAndSend(cancelOrderQueue, orderId);
    }
}
