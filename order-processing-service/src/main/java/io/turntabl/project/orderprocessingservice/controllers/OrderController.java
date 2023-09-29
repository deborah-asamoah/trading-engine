package io.turntabl.project.orderprocessingservice.controllers;

import io.turntabl.project.orderprocessingapi.dtos.OrderDto;
import io.turntabl.project.orderprocessingapi.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.orderprocessingapi.dtos.responsebodies.ListOrdersResponseBody;
import io.turntabl.project.orderprocessingapi.dtos.responsebodies.OrderCreatedResponseBody;
import io.turntabl.project.orderprocessingservice.mappers.CreateOrderRequestBodyMapper;
import io.turntabl.project.orderprocessingservice.mappers.OrderViewMapper;
import io.turntabl.project.orderprocessingservice.services.OrderService;
import io.turntabl.project.orderprocessingservice.services.OrderViewService;
import io.turntabl.project.orderprocessingservice.services.PortfolioService;
import io.turntabl.project.orderprocessingservice.utils.OrderLoggingUtils;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.reportingcontract.enums.OrderEventType;
import jakarta.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {
    private final CreateOrderRequestBodyMapper orderRequestBodyMapper;
    private final OrderService orderService;
    private final OrderViewService orderViewService;
    private final PortfolioService portfolioService;
    private final OrderViewMapper orderViewMapper;
    private final OrderLoggingUtils orderLoggingUtils;

    public OrderController(CreateOrderRequestBodyMapper orderRequestBodyMapper,
                           OrderService orderService,
                           OrderViewService orderViewService,
                           PortfolioService portfolioService,
                           OrderViewMapper orderViewMapper,
                           OrderLoggingUtils orderLoggingUtils) {
        this.orderRequestBodyMapper = orderRequestBodyMapper;
        this.orderService = orderService;
        this.orderViewService = orderViewService;
        this.portfolioService = portfolioService;
        this.orderViewMapper = orderViewMapper;
        this.orderLoggingUtils = orderLoggingUtils;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ListOrdersResponseBody readOrders(@QueryParam("client") String client) {
        return ListOrdersResponseBody
                .builder()
                .data(orderService
                        .findByClient(UUID.fromString(client))
                        .stream()
                        .map(orderViewMapper::toOrderViewDto)
                        .toList())
                .build();
    }

    @GetMapping("/open")
    @ResponseStatus(HttpStatus.OK)
    ListOrdersResponseBody readOpenOrders() {
        return ListOrdersResponseBody
                .builder()
                .data(orderViewService
                        .findOpenOrders()
                        .stream()
                        .map(orderViewMapper::toOrderViewDto)
                        .toList())
                .build();
    }

    @GetMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    ListOrdersResponseBody readFilledOrders() {
        return ListOrdersResponseBody
                .builder()
                .data(orderViewService
                        .findFilledOrders()
                        .stream()
                        .map(orderViewMapper::toOrderViewDto)
                        .toList())
                .build();
    }

    @GetMapping("/cancelled")
    @ResponseStatus(HttpStatus.OK)
    ListOrdersResponseBody readCancelledOrders() {
        return ListOrdersResponseBody
                .builder()
                .data(orderViewService
                        .findCancelledOrders()
                        .stream()
                        .map(orderViewMapper::toOrderViewDto)
                        .toList())
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderCreatedResponseBody createOrder(@RequestBody CreateOrderRequestBody body) {
        Order order = orderRequestBodyMapper.toOrder(body);
        order.setPortfolio(portfolioService.findById(body.getPortfolioId()));
        String orderId = orderService.createOrder(order);
        orderLoggingUtils.logEvent(OrderEventType.CREATE_ORDER,UUID.fromString(orderId),
                "ORDER FOR " + order.getProduct() + " CREATED");
        return new OrderCreatedResponseBody(orderId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void cancelOrder(@PathVariable("id") UUID id) {
        Order order = orderService.findById(id);
        orderService.cancelOrder(order);
        orderLoggingUtils.logEvent(OrderEventType.CANCELED,order.getId(),
                "ORDER FOR " + order.getProduct() + " CANCELLED");
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    OrderDto readOrderById(@PathVariable("id") UUID id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }
}
