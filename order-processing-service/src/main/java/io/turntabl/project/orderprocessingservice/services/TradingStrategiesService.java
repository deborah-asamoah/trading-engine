package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.orderprocessingservice.utils.PricingStrategyComparators;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TradingStrategiesService {

    private final MarketDataService marketDataService;

    public TradingStrategiesService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    public Queue<TradingParameters> compileTradingParameters(Order order) {
        Queue<TradingParameters> options = new PriorityQueue<>(PricingStrategyComparators
                .createComparator(order));

        for (var exchange : Exchange.values()) {
            MarketDataCache marketData = marketDataService
                    .findByExchangeAndProduct(exchange,
                            order.getProduct());

            List<OrderBook> orderBook = marketDataService
                    .findOpenOrdersByExchangeAndProductAndOrderSide(exchange,
                            order.getProduct(),
                            order.getSide().inverse());

            TradingParameters tradingParameters = TradingParameters
                    .builderWith(marketData,
                            orderBook,
                            exchange)
                    .order(order)
                    .build();

            options.offer(tradingParameters);
        }

        return options;
    }

    public Queue<OrderLeg> compileOrderLegs(Order order, Queue<TradingParameters> tradingParameters) {
        Queue<OrderLeg> orderLegs = new ArrayDeque<>();

        OrderType type = order.getType();
        OrderSide side = order.getSide();

        int unfilledQuantity = order.getQuantity();

        // Fill Based On Unfilled Quantities At Exchange (For Inverse OrderType)
        while (unfilledQuantity > 0 && !tradingParameters.isEmpty()) {
            TradingParameters option = tradingParameters.poll();

            int unfilledQuantityExchange = type == OrderType.LIMIT
                    ? option.getUnfilledMarketQuantity()
                    : option.getUnfilledLimitQuantity();

            int quantity = Math.min(unfilledQuantity, unfilledQuantityExchange);
            if (quantity == 0 && !orderLegs.isEmpty())
                continue;

            double price = side == OrderSide.SELL
                    ? option.getHighestPricePossible()
                    : option.getLowestPricePossible();

            OrderLeg orderLeg = OrderLeg
                    .builder()
                    .price(price)
                    .order(order)
                    .exchange(option.getExchange())
                    .quantity(quantity)
                    .build();

            orderLegs.offer(orderLeg);

            unfilledQuantity -= quantity;
        }

        // Limit Matching Algorithm
        if (unfilledQuantity > 0 && type == OrderType.LIMIT) {
            List<OrderBook> orderBook = new ArrayList<>(marketDataService
                    .findOpenLimitOrdersByProductAndOrderSide(order.getProduct(),
                            order.getSide().inverse()));

            // Sort
            Comparator<OrderBook> comparator = Comparator.comparingDouble(OrderBook::getPrice);
            if (side == OrderSide.SELL)
                comparator = comparator.reversed();

            orderBook.sort(comparator);

            Iterator<OrderBook> itr = orderBook.iterator();
            while (unfilledQuantity > 0 && itr.hasNext()) {
                OrderBook exchangeOrder = itr.next();

                int quantity = Math.min(unfilledQuantity, exchangeOrder.getQuantity());

                OrderLeg orderLeg = OrderLeg
                        .builder()
                        .price(exchangeOrder.getPrice())
                        .order(order)
                        .exchange(exchangeOrder.getExchange())
                        .quantity(quantity)
                        .build();

                orderLegs.offer(orderLeg);

                unfilledQuantity -= quantity;
            }
        }

        // Place Remaining Unfilled Quantity With Exchange With The Best Price (When Market OrderType)
        if (unfilledQuantity > 0) {
            OrderLeg exchangeWithBestPrice = orderLegs.peek();
            exchangeWithBestPrice
                    .setQuantity(exchangeWithBestPrice.getQuantity() + unfilledQuantity);
        }

        // Drop Exchange With Best Price When
        // There Aren't Any Unfilled Quantity
        // & There Isn't A Remainder
        if (orderLegs.peek().getQuantity() == 0)
            orderLegs.poll();

        return orderLegs;
    }

}
