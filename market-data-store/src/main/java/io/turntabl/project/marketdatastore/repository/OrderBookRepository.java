package io.turntabl.project.marketdatastore.repository;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderBookRepository extends CrudRepository<OrderBook, UUID> {
    List<OrderBook> findAllByProductAndExchange(String product, Exchange exchange);

    List<OrderBook> findAllByProductAndExchangeAndOrderSide(String product, Exchange exchange, OrderSide orderSide);

    List<OrderBook> findAllByProductAndOrderSideAndOrderType(String product,
                                                             OrderSide orderSide,
                                                             OrderType orderType);
}
