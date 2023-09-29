package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.persistence.entities.Order;

public class OptimumPriceComputer {

    public static double resolveMaxPriceShift(MarketDataCache marketDataCache) {
        double maxPriceShift = marketDataCache.getMaxPriceShift();

        if (maxPriceShift == 1)
            return marketDataCache.getLastTradedPrice() * 0.1;

        return maxPriceShift;
    }

    public static double computeOptimumPrice(Order order,
                                             TradingParameters tradingParameters) {
        OrderSide side = order.getSide();

        double price = order.getPrice();

        double lastTradedPrice = tradingParameters
                .getMarketData()
                .getLastTradedPrice();

        double maxPriceShift = tradingParameters
                .getMarketData()
                .getMaxPriceShift();

        // Buy As Low As Possible (But Not Beyond Ask Price)
        if (side == OrderSide.BUY) {
            double askPrice = tradingParameters
                    .getMarketData()
                    .getAskPrice();

            if(maxPriceShift == 1) {
                double priceShift = (0.1 * lastTradedPrice);
                double lowestBidPrice = lastTradedPrice - priceShift;
                double highestBidPrice = lastTradedPrice + priceShift;
                if(price >= lowestBidPrice && price <= highestBidPrice) {
                    return price;
                }
                if(askPrice >= lowestBidPrice && askPrice <= highestBidPrice) {
                    return askPrice;
                }
                return lowestBidPrice;
            }

            double asLowAsPossible = lastTradedPrice - maxPriceShift;
            double highestPossible = lastTradedPrice + maxPriceShift;
            double lastAndAskPriceDifference = Math.abs(lastTradedPrice - askPrice);

            // Return price If Reasonable
            if (price >= asLowAsPossible && price <= highestPossible)
                return price;

            // Otherwise Aim To Get It Matched
            return askPrice == 0
                    ? asLowAsPossible
                    : lastAndAskPriceDifference <= maxPriceShift ? askPrice : asLowAsPossible;
        }

        // Sell As High As Possible (But Not Beyond Bid Price)
        if (side == OrderSide.SELL) {
            double bidPrice = tradingParameters
                    .getMarketData()
                    .getBidPrice();

            double asHighAsPossble = lastTradedPrice + maxPriceShift;

            // Return price If Reasonable
            if (price <= asHighAsPossble)
                return Math.max(price, bidPrice);

            // Otherwise Aim To Get It Matched
            return bidPrice == 0
                    ? asHighAsPossble
                    : bidPrice;
        }

        throw new UnsupportedOperationException(String
                .format("Cannot Optimise Price For Order On %s Side", side));
    }

}
