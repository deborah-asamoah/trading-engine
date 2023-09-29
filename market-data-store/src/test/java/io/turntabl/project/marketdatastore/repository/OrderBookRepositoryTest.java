package io.turntabl.project.marketdatastore.repository;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.config.RedisProperties;
import io.turntabl.project.marketdatastore.config.RedisTestConfig;
import io.turntabl.project.marketdatastore.config.RedisTestServerConfig;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RedisTestConfig.class, RedisProperties.class, RedisTestServerConfig.class})
class OrderBookRepositoryTest {
    @Autowired
    private OrderBookRepository underTest;

    @BeforeEach
    void setUp() {
        List<OrderBook> orderBooks = List.of(
                OrderBook.builder()
                        .orderId(UUID.fromString("e0dc7742-f660-42d0-ad0c-0f0e358d2465"))
                        .price(22.0)
                        .product("GOOGL")
                        .cumulativePrice(5)
                        .orderSide(OrderSide.SELL)
                        .exchange(Exchange.MAL2)
                        .orderType(OrderType.LIMIT)
                        .quantity(19)
                        .cumulativeQuantity(10)
                        .build(),
                OrderBook.builder()
                        .orderId(UUID.fromString("e0dc7742-f660-42d0-ad0c-0f0e358d2df7"))
                        .price(22.0)
                        .product("IBM")
                        .cumulativePrice(5)
                        .orderSide(OrderSide.SELL)
                        .exchange(Exchange.MAL2)
                        .orderType(OrderType.LIMIT)
                        .quantity(19)
                        .cumulativeQuantity(10)
                        .build(),
                OrderBook.builder()
                        .orderId(UUID.fromString("e0dc7742-f660-42d0-ad0c-0f0e358d2df1"))
                        .price(22.0)
                        .product("GOOGL")
                        .cumulativePrice(5)
                        .orderSide(OrderSide.SELL)
                        .exchange(Exchange.MAL2)
                        .orderType(OrderType.LIMIT)
                        .quantity(19)
                        .cumulativeQuantity(10)
                        .build()
        );
        underTest.saveAll(orderBooks);
    }

    @AfterEach()
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testBeanIsInitialised() {
        assertNotNull(underTest);
    }

    @Test
    void testGetOrdersByProductAndExchange() {
        List<OrderBook> result = underTest.findAllByProductAndExchange("GOOGL", Exchange.MAL2);

        assertNotNull(result);
        assertEquals(2,result.size());
    }
}