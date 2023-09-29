package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.exchangeclient.ExchangeClient;
import io.turntabl.project.exchangeclient.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.exchangeclient.dtos.requestbodies.UpdateOrderRequestBody;
import io.turntabl.project.orderprocessingapi.dtos.requestbodies.UpdateOrderLegRequestBody;
import io.turntabl.project.orderprocessingservice.factories.ExchangeClientFactory;
import io.turntabl.project.orderprocessingservice.mappers.OrderLegMapper;
import io.turntabl.project.persistence.entities.OrderLeg;
import io.turntabl.project.persistence.repositories.OrderLegRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderLegService {
    private final OrderLegRepository orderLegRepository;
    private final ExchangeClientFactory exchangeClientFactory;
    private final OrderLegMapper orderLegMapper;

    public OrderLegService(OrderLegRepository orderLegRepository,
                           ExchangeClientFactory exchangeClientFactory,
                           OrderLegMapper orderLegMapper) {
        this.orderLegRepository = orderLegRepository;
        this.exchangeClientFactory = exchangeClientFactory;
        this.orderLegMapper = orderLegMapper;
    }

    public OrderLeg findByExchangeOrderId(String exchangeOrderId) {
        return orderLegRepository
                .findByExchangeOrderId(exchangeOrderId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("OrderLeg With exchangeOrderId %s Not Found", exchangeOrderId)));
    }

    public static void validateUpdateOrderLeg(OrderLeg orderLeg, UpdateOrderLegRequestBody update) {
        if (orderLeg.isCancelled())
            throw new IllegalArgumentException("Cannot Update A Cancelled OrderLeg");

        int delta = update.getQuantity() - orderLeg.getQuantity();
        if (orderLeg.getBalance() + delta < 0)
            throw new IllegalArgumentException("Cannot Reduce Quantity Past Remaining Balance");
    }

    public OrderLeg findById(UUID id) {
        return orderLegRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("OrderLeg With ID %s Not Found", id)));
    }

    public OrderLeg updateOrderLeg(OrderLeg orderLeg, UpdateOrderLegRequestBody update) {
        OrderLegService.validateUpdateOrderLeg(orderLeg, update);

        ExchangeClient exchangeClient = exchangeClientFactory
                .getExchangeClientById(orderLeg.getExchange());

        boolean isUpdated = exchangeClient.updateOrder(orderLeg.getExchangeOrderId(),
                UpdateOrderRequestBody
                        .builder()
                        .price(update.getPrice())
                        .side(orderLeg.getOrder().getSide())
                        .type(orderLeg.getOrder().getType())
                        .product(orderLeg.getOrder().getProduct())
                        .quantity(update.getQuantity())
                        .build());

        if (!isUpdated)
            throw new RuntimeException("Exchange Refused To Update OrderLeg");

        orderLeg.setPrice(update.getPrice());
        orderLeg.setQuantity(update.getQuantity());

        return orderLeg;
    }

    public OrderLeg createOrderLeg(OrderLeg orderLeg) {
        ExchangeClient exchangeClient = exchangeClientFactory
                .getExchangeClientById(orderLeg
                        .getExchange());

        CreateOrderRequestBody createOrderRequestBody = orderLegMapper
                .toCreateOrderRequestBody(orderLeg);

        String orderId = exchangeClient
                .createOrder(createOrderRequestBody)
                .replaceAll("\"", "");
        orderLeg.setExchangeOrderId(orderId);

        return orderLegRepository.save(orderLeg);
    }

}
