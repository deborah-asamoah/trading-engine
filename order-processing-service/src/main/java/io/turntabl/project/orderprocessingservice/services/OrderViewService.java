package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.persistence.entities.OrderView;
import io.turntabl.project.persistence.repositories.OrderViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderViewService {
    private final OrderViewRepository orderViewRepository;

    public OrderViewService(OrderViewRepository orderViewRepository) {
        this.orderViewRepository = orderViewRepository;
    }

    public List<OrderView> findOpenOrders() {
        return orderViewRepository.findOpenOrders();
    }

    public List<OrderView> findFilledOrders() {
        return orderViewRepository.findFilledOrders();
    }

    public List<OrderView> findCancelledOrders() {
        return orderViewRepository.findCancelledOrders();
    }
}
