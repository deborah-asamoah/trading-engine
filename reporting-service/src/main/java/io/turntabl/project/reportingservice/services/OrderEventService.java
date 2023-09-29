package io.turntabl.project.reportingservice.services;

import io.turntabl.project.reportingcontract.entities.OrderEvent;
import io.turntabl.project.reportingcontract.repositories.OrderEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;

    public OrderEventService(OrderEventRepository orderEventRepository) {
        this.orderEventRepository = orderEventRepository;
    }

    public List<OrderEvent> findAll() {
        return orderEventRepository
                .findByOrderByHappenedOnDesc();
    }

    public void save(OrderEvent orderEvent) {
        orderEventRepository.save(orderEvent);
    }
}
