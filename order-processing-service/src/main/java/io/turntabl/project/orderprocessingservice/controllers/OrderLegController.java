package io.turntabl.project.orderprocessingservice.controllers;

import io.turntabl.project.orderprocessingapi.dtos.OrderLegDto;
import io.turntabl.project.orderprocessingapi.dtos.requestbodies.UpdateOrderLegRequestBody;
import io.turntabl.project.orderprocessingservice.mappers.OrderLegMapper;
import io.turntabl.project.orderprocessingservice.services.OrderLegService;
import io.turntabl.project.orderprocessingservice.utils.OrderLoggingUtils;
import io.turntabl.project.persistence.entities.OrderLeg;
import io.turntabl.project.reportingcontract.enums.OrderEventType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("order-legs")
@CrossOrigin
public class OrderLegController {
    private final OrderLegService orderLegService;
    private final OrderLegMapper orderLegMapper;
    private final OrderLoggingUtils orderLoggingUtils;

    public OrderLegController(OrderLegService orderLegService,
                              OrderLegMapper orderLegMapper,
                              OrderLoggingUtils orderLoggingUtils) {
        this.orderLegService = orderLegService;
        this.orderLegMapper = orderLegMapper;
        this.orderLoggingUtils = orderLoggingUtils;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    OrderLegDto updateOrder(@PathVariable("id") UUID id,
                            UpdateOrderLegRequestBody body) {
        OrderLeg orderLeg = orderLegService.findById(id);

        // Install Defaults
        if (body.getPrice() == null)
            body.setPrice(orderLeg.getPrice());
        if (body.getQuantity() == null)
            body.setQuantity(orderLeg.getQuantity());

        orderLeg = orderLegService.updateOrderLeg(orderLeg, body);
        orderLoggingUtils.logEvent(OrderEventType.UPDATED,id,"ORDER: " + id + " UPDATED");
        return orderLegMapper.toOrderLegDto(orderLeg);
    }
}