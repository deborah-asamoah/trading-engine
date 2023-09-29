package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.persistence.entities.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

public class PricingStrategyComparatorsTests {

    @Test
    void Given_MarketOrder_When_OnBuySide_Then_PrioritizeExchangeWithLowestHighestPricePossible() {
        Order order = new Order();
        order.setType(OrderType.MARKET);
        order.setSide(OrderSide.BUY);

        Queue<TradingParameters> options = new PriorityQueue<>(PricingStrategyComparators
                .createComparator(order));


        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(2)
                                        .build(),
                                List.of(),
                                Exchange.MAL1)
                        .build());

        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(3)
                                        .build(),
                                List.of(),
                                Exchange.MAL2)
                        .build());

        assertThat(options
                .poll()
                .getExchange())
                .isEqualTo(Exchange.MAL1);
    }

    @Test
    void Given_MarketOrder_When_OnSellSide_Then_PrioritizeExchangeWithHighestLowestPricePossible() {
        Order order = new Order();
        order.setType(OrderType.MARKET);
        order.setSide(OrderSide.SELL);

        Queue<TradingParameters> options = new PriorityQueue<>(PricingStrategyComparators
                .createComparator(order));


        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(2)
                                        .build(),
                                List.of(),
                                Exchange.MAL1)
                        .build());

        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(3)
                                        .build(),
                                List.of(),
                                Exchange.MAL2)
                        .build());

        assertThat(options
                .poll()
                .getExchange())
                .isEqualTo(Exchange.MAL1);
    }

    @Test
    void Given_LimitOrder_When_OnBuySide_Then_PrioritizeExchangeWithLowestLowestPricePossible() {
        Order order = new Order();
        order.setType(OrderType.LIMIT);
        order.setSide(OrderSide.BUY);
        order.setPrice(2);

        Queue<TradingParameters> options = new PriorityQueue<>(PricingStrategyComparators
                .createComparator(order));


        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(2)
                                        .build(),
                                List.of(),
                                Exchange.MAL1)
                        .build());

        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(3)
                                        .build(),
                                List.of(),
                                Exchange.MAL2)
                        .build());

        assertThat(options
                .poll()
                .getExchange())
                .isEqualTo(Exchange.MAL2);
    }

    @Test
    void Given_LimitOrder_When_OnSellSide_Then_PrioritizeExchangeWithHighestHighestPricePossible() {
        Order order = new Order();
        order.setType(OrderType.LIMIT);
        order.setSide(OrderSide.SELL);
        order.setPrice(2);

        Queue<TradingParameters> options = new PriorityQueue<>(PricingStrategyComparators
                .createComparator(order));


        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(2)
                                        .build(),
                                List.of(),
                                Exchange.MAL1)
                        .build());

        options
                .offer(TradingParameters
                        .builderWith(MarketDataCache
                                        .builder()
                                        .lastTradedPrice(10)
                                        .maxPriceShift(3)
                                        .build(),
                                List.of(),
                                Exchange.MAL2)
                        .build());

        assertThat(options
                .poll()
                .getExchange())
                .isEqualTo(Exchange.MAL2);
    }

}
