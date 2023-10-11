package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.orderprocessingapi.dtos.requestbodies.UpdateOrderLegRequestBody;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.factories.ExchangeClientFactory;
import io.turntabl.project.orderprocessingservice.mappers.OrderLegMapper;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderLeg;
import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.OrderLegRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderLegServiceTest {
    @InjectMocks
    OrderLegService underTest;

    @Mock
    OrderLegRepository orderLegRepository;

    @Mock
    ExchangeClientFactory exchangeClientFactory;

    @Mock
    OrderLegMapper orderLegMapper;

    OrderLeg orderLeg;
    Order order;
    Portfolio portfolio;
    UpdateOrderLegRequestBody updateOrderLegRequestBody;
    UUID uuid;


    @BeforeEach
    void setUp (){
        uuid = UUID.fromString("48c05348-5d7a-44af-a821-f93fe9ccb106");

        portfolio = new Portfolio();

        updateOrderLegRequestBody = new UpdateOrderLegRequestBody();
        updateOrderLegRequestBody.setPrice(15.0);

    }

    @Test
    void validateUpdateOrderLegIfOrderLegIsNotCancelled (){
        order = new Order(uuid,"MSFT", 20, 10.0, OrderSide.BUY, OrderType.LIMIT, portfolio, false);

        orderLeg = new OrderLeg(uuid, order, 10, 10.0, "Exchangeorder1", Exchange.MAL1, 10, 10.0, false);
        updateOrderLegRequestBody.setQuantity(30);

        OrderLegService.validateUpdateOrderLeg(orderLeg, updateOrderLegRequestBody);
        assertDoesNotThrow(() -> IllegalArgumentException.class);
    }

    @Test
    void validateOrderLegIfOrderLegIsCancelled (){
        order = new Order(uuid,"MSFT", 20, 10.0, OrderSide.BUY, OrderType.LIMIT, portfolio, false);

        orderLeg = new OrderLeg(uuid, order, 10, 10.0, "Exchangeorder1", Exchange.MAL1, 10, 10.0, true);

        updateOrderLegRequestBody.setQuantity(30);

        assertThrows(IllegalArgumentException.class, () -> OrderLegService.validateUpdateOrderLeg(orderLeg, updateOrderLegRequestBody));
    }

    @Test
    void validateOrderLegIfTheSumOfOrderLegBalanceAndDeltaIsLessThanZero (){
        order = new Order(uuid,"MSFT", 20, 10.0, OrderSide.BUY, OrderType.LIMIT, portfolio, false);

        orderLeg = new OrderLeg(uuid, order, 9, 10.0, "Exchangeorder1", Exchange.MAL1, 10, 10.0, true);

        updateOrderLegRequestBody.setQuantity(9);

        assertThrows(IllegalArgumentException.class, () -> OrderLegService.validateUpdateOrderLeg(orderLeg, updateOrderLegRequestBody));
    }

}