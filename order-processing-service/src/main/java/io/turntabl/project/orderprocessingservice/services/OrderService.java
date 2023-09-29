package io.turntabl.project.orderprocessingservice.services;


import io.turntabl.project.orderprocessingservice.events.OrderCancelledEvent;
import io.turntabl.project.orderprocessingservice.events.OrderCreatedEvent;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderView;
import io.turntabl.project.persistence.repositories.OrderRepository;
import io.turntabl.project.persistence.repositories.OrderViewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderViewRepository orderViewRepository;

    private final ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderViewRepository orderViewRepository,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderViewRepository = orderViewRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String createOrder(Order order) {
        Order createdOrder = orderRepository.save(order);

        OrderCreatedEvent createdOrderEvent = new OrderCreatedEvent(createdOrder);
        applicationEventPublisher.publishEvent(createdOrderEvent);
        return createdOrder.getId().toString();
    }

    public void cancelOrder(Order order) {
        order.setCancelled(true);

        applicationEventPublisher
                .publishEvent(new OrderCancelledEvent(order));
    }

    public Order findById(UUID id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Order With ID %s Not Found", id)));
    }

    public List<OrderView> findByClient(UUID client) {
        return orderViewRepository
                .findByClient(client);
    }


}
