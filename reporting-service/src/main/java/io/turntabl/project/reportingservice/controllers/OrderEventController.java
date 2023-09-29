package io.turntabl.project.reportingservice.controllers;


import io.turntabl.project.reportingcontract.entities.OrderEvent;
import io.turntabl.project.reportingservice.services.OrderEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order-events")
public class OrderEventController {

    private final OrderEventService orderEventService;

    public OrderEventController(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @GetMapping
    List<OrderEvent> readOrderEvents() {
        return orderEventService.findAll();
    }
}
