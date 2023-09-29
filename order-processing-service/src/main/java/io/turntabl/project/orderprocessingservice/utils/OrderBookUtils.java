package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.marketdatastore.entity.OrderBook;

import java.util.List;
import java.util.function.Predicate;

public class OrderBookUtils {

    public static int getUnfilledQuantity(OrderBook order) {
        return order.getQuantity() - order.getCumulativeQuantity();
    }

    public static int getUnfilledQuantity(List<OrderBook> orderBook, Predicate<OrderBook> predicate) {
        return orderBook
                .stream()
                .filter(predicate)
                .mapToInt(OrderBookUtils::getUnfilledQuantity)
                .sum();
    }

    public static int getUnfilledQuantity(List<OrderBook> orderBook) {
        return getUnfilledQuantity(orderBook, e -> true);
    }

}
