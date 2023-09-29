package io.turntabl.project.orderprocessingservice.models;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.utils.OptimumPriceComputer;
import io.turntabl.project.orderprocessingservice.utils.OrderBookUtils;
import io.turntabl.project.persistence.entities.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingParameters {
    private Exchange exchange;
    private MarketDataCache marketData;
    private Order order;

    private int unfilledQuantity;
    private int unfilledMarketQuantity;
    private int unfilledLimitQuantity;

    private double lowestPricePossible;
    private double highestPricePossible;

    public static TradingParameters.TradingParametersBuilder builderWith(MarketDataCache marketData,
                                                                         List<OrderBook> orderBook,
                                                                         Exchange exchange) {

        double maxPriceShift = OptimumPriceComputer.resolveMaxPriceShift(marketData);
        double lastTradedPrice = marketData.getLastTradedPrice();

        // Highest Price Possible
        double highestPricePossible = lastTradedPrice + maxPriceShift;

        // Lowest Price Possible
        double lowestPricePossible = lastTradedPrice - maxPriceShift;
        if (lowestPricePossible <= 0)
            lowestPricePossible = 0.01;

        // Quantity
        int unfilledQuantity = OrderBookUtils.getUnfilledQuantity(orderBook);
        int unfilledMarketQuantity = OrderBookUtils.getUnfilledQuantity(orderBook,
                e -> OrderType.MARKET == e.getOrderType());
        int unfilledLimitQuantity = OrderBookUtils.getUnfilledQuantity(orderBook,
                e -> OrderType.LIMIT == e.getOrderType());

        return TradingParameters
                .builder()
                .exchange(exchange)
                .marketData(marketData)
                .unfilledQuantity(unfilledQuantity)
                .unfilledLimitQuantity(unfilledLimitQuantity)
                .unfilledMarketQuantity(unfilledMarketQuantity)
                .highestPricePossible(highestPricePossible)
                .lowestPricePossible(lowestPricePossible);
    }
}
