package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradingStrategiesServiceTests {
    @Mock
    MarketDataService marketDataService;
    @InjectMocks
    TradingStrategiesService tradingStrategiesService;

    @Test
    void Given_2ExchangesWithMarketQuantity_When_CompileOrderLegs_Then_UseExchangeWithBestPrice() {
        String product = "IBM";

        Order order = Order
                .builder()
                .product(product)
                .type(OrderType.LIMIT)
                .side(OrderSide.BUY)
                .price(10)
                .quantity(100)
                .build();


        // MAL1
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL1, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL1,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(100)
                        .build()));

        // MAL2
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL2, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(3)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL2,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(100)
                        .build()));

        Queue<TradingParameters> tradingParameters = tradingStrategiesService
                .compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService
                .compileOrderLegs(order,
                        tradingParameters);

        assertThat(orderLegs.size())
                .isEqualTo(1);

        OrderLeg orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getQuantity()).isEqualTo(100);
    }

    @Test
    void Given_1StExchangeWithPartialQuantity_When_CompileOrderLegs_Then_PlaceAvailableQuantityAtExchange1AndRemainderAtExchange2() {
        String product = "IBM";

        Order order = Order
                .builder()
                .product(product)
                .type(OrderType.LIMIT)
                .side(OrderSide.BUY)
                .price(10)
                .quantity(100)
                .build();


        // MAL1
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL1, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL1,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(100)
                        .build()));

        // MAL2
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL2, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(3)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL2,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        Queue<TradingParameters> tradingParameters = tradingStrategiesService
                .compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService
                .compileOrderLegs(order,
                        tradingParameters);

        assertThat(orderLegs.size())
                .isEqualTo(2);

        OrderLeg orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getQuantity()).isEqualTo(40);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getQuantity()).isEqualTo(60);
    }

    @Test
    void Given_2ExchangesWithPartialQuantity_When_CompileOrderLegs_Then_PlaceAvailableQuantityAtBothExchangesAndRemainderAtExchangeWithBestPrice() {
        String product = "IBM";

        Order order = Order
                .builder()
                .product(product)
                .type(OrderType.LIMIT)
                .side(OrderSide.BUY)
                .price(10)
                .quantity(100)
                .build();


        // MAL1
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL1, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL1,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        // MAL2
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL2, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(3)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL2,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        Queue<TradingParameters> tradingParameters = tradingStrategiesService
                .compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService
                .compileOrderLegs(order,
                        tradingParameters);

        assertThat(orderLegs.size())
                .isEqualTo(2);

        OrderLeg orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getQuantity()).isEqualTo(60);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getQuantity()).isEqualTo(40);
    }

    @Test
    void Given_2ExchangesWithPartialQuantityAndLimitOrders_When_CompileOrderLegs_Then_PlaceAvailableQuantityAtBothExchangesAndRemainderAsLimitOrders() {
        String product = "IBM";

        Order order = Order
                .builder()
                .product(product)
                .type(OrderType.LIMIT)
                .side(OrderSide.BUY)
                .price(10)
                .quantity(100)
                .build();


        // MAL1
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL1, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL1,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        // MAL2
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL2, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(3)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL2,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        // Limit Orders
        when(marketDataService
                .findOpenLimitOrdersByProductAndOrderSide(product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                                .builder()
                                .exchange(Exchange.MAL2)
                                .price(12)
                                .orderType(OrderType.LIMIT)
                                .quantity(10)
                                .build(),
                        OrderBook
                                .builder()
                                .exchange(Exchange.MAL1)
                                .price(11)
                                .orderType(OrderType.LIMIT)
                                .quantity(10)
                                .build()));

        Queue<TradingParameters> tradingParameters = tradingStrategiesService
                .compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService
                .compileOrderLegs(order,
                        tradingParameters);

        assertThat(orderLegs.size())
                .isEqualTo(4);

        OrderLeg orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getQuantity()).isEqualTo(40);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getQuantity()).isEqualTo(40);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getPrice()).isEqualTo(11);
        assertThat(orderLeg.getQuantity()).isEqualTo(10);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getPrice()).isEqualTo(12);
        assertThat(orderLeg.getQuantity()).isEqualTo(10);
    }

    @Test
    void Given_2ExchangesWithPartialQuantityAndLimitOrders_When_CompileOrderLegs_Then_PlaceAvailableQuantityAtBothExchangesAndThenLimitOrdersAndRemainderAtExchangeWithBestPrice() {
        String product = "IBM";

        Order order = Order
                .builder()
                .product(product)
                .type(OrderType.LIMIT)
                .side(OrderSide.BUY)
                .price(10)
                .quantity(100)
                .build();


        // MAL1
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL1, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL1,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        // MAL2
        when(marketDataService
                .findByExchangeAndProduct(Exchange.MAL2, product))
                .thenReturn(MarketDataCache
                        .builder()
                        .maxPriceShift(3)
                        .lastTradedPrice(10)
                        .build());
        when(marketDataService
                .findOpenOrdersByExchangeAndProductAndOrderSide(Exchange.MAL2,
                        product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .orderType(OrderType.MARKET)
                        .quantity(40)
                        .build()));

        // Limit Orders
        when(marketDataService
                .findOpenLimitOrdersByProductAndOrderSide(product,
                        OrderSide.SELL))
                .thenReturn(List.of(OrderBook
                        .builder()
                        .exchange(Exchange.MAL1)
                        .price(11)
                        .orderType(OrderType.LIMIT)
                        .quantity(10)
                        .build()));

        Queue<TradingParameters> tradingParameters = tradingStrategiesService
                .compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService
                .compileOrderLegs(order,
                        tradingParameters);

        assertThat(orderLegs.size())
                .isEqualTo(3);

        OrderLeg orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL2);
        assertThat(orderLeg.getQuantity()).isEqualTo(50);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getQuantity()).isEqualTo(40);

        orderLeg = orderLegs.poll();
        assertThat(orderLeg.getExchange()).isEqualTo(Exchange.MAL1);
        assertThat(orderLeg.getPrice()).isEqualTo(11);
        assertThat(orderLeg.getQuantity()).isEqualTo(10);
    }

}
