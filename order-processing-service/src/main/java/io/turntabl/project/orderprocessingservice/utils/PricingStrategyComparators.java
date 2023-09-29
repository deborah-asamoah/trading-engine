package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.persistence.entities.Order;

import java.util.Comparator;

public class PricingStrategyComparators {

    /**
     * Highest Lowest Price Possible
     *
     * @return
     */
    private static final Comparator<TradingParameters> MARKET_SELL_COMPARATOR = Comparator
            .<TradingParameters>comparingDouble(a -> a
                    .getLowestPricePossible())
            .reversed();
    /**
     * Lowest Highest Price Possible
     *
     * @return
     */
    private static final Comparator<TradingParameters> MARKET_BUY_COMPARATOR = Comparator
            .comparingDouble(a -> a
                    .getHighestPricePossible());

    /**
     * Highest, Highest Price Possible
     *
     * @return
     */
    private static final Comparator<TradingParameters> LIMIT_SELL_COMPARATOR = Comparator
            .<TradingParameters>comparingDouble(a -> a
                    .getHighestPricePossible())
            .reversed();
    /**
     * Lowest, Lowest Price Possible
     *
     * @return
     */
    private static final Comparator<TradingParameters> LIMIT_BUY_COMPARATOR = Comparator
            .comparingDouble(a -> a
                    .getLowestPricePossible());

    public static Comparator<TradingParameters> createComparator(Order order) {
        if (order.getType() == OrderType.MARKET) {
            if (order.getSide() == OrderSide.BUY)
                return MARKET_BUY_COMPARATOR;

            if (order.getSide() == OrderSide.SELL)
                return MARKET_SELL_COMPARATOR;
        }

        if (order.getType() == OrderType.LIMIT) {
            if (order.getSide() == OrderSide.BUY)
                return LIMIT_BUY_COMPARATOR;

            if (order.getSide() == OrderSide.SELL)
                return LIMIT_SELL_COMPARATOR;
        }

        throw new UnsupportedOperationException(String
                .format("Could Not Create Comparator For [%s, %s]",
                        order.getType(),
                        order.getSide()));
    }

}
