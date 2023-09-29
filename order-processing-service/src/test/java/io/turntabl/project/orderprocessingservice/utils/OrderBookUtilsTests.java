package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderBookUtilsTests {

    @Test
    void Given_OrderBookEntry_When_UnfilledQuantityIs2_Then_2() {
        OrderBook order = OrderBook
                .builder()
                .quantity(4)
                .cumulativeQuantity(2)
                .build();

        assertThat(OrderBookUtils.getUnfilledQuantity(order))
                .isEqualTo(2);
    }

    @Test
    void Given_OrderBook_When_UnfilledQuantityIs6_Then_6() {
        List<OrderBook> orderBook = List.of(OrderBook
                .builder()
                .quantity(4)
                .cumulativeQuantity(2)
                .build(), OrderBook
                .builder()
                .quantity(6)
                .cumulativeQuantity(2)
                .build());

        assertThat(OrderBookUtils.getUnfilledQuantity(orderBook))
                .isEqualTo(6);
    }

    @Test
    void Given_OrderBook_When_UnfilledMarketQuantityIs2_Then_2() {
        List<OrderBook> orderBook = List.of(OrderBook
                .builder()
                .quantity(4)
                .orderType(OrderType.MARKET)
                .cumulativeQuantity(2)
                .build(), OrderBook
                .builder()
                .quantity(6)
                .orderType(OrderType.LIMIT)
                .cumulativeQuantity(2)
                .build());

        assertThat(OrderBookUtils.getUnfilledQuantity(orderBook, e -> e.getOrderType() == OrderType.MARKET))
                .isEqualTo(2);
    }

    @Test
    void Given_OrderBook_When_UnfilledLimitQuantityIs4_Then_4() {
        List<OrderBook> orderBook = List.of(OrderBook
                .builder()
                .quantity(4)
                .orderType(OrderType.MARKET)
                .cumulativeQuantity(2)
                .build(), OrderBook
                .builder()
                .quantity(6)
                .orderType(OrderType.LIMIT)
                .cumulativeQuantity(2)
                .build());

        assertThat(OrderBookUtils.getUnfilledQuantity(orderBook, e -> e.getOrderType() == OrderType.LIMIT))
                .isEqualTo(4);
    }

}
