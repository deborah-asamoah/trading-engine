package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.ExchangeMarketDataCache;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.marketdatastore.repository.ExchangeMarketDataCacheRepository;
import io.turntabl.project.marketdatastore.repository.OrderBookRepository;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketDataService {

    private final ExchangeMarketDataCacheRepository marketDataRepository;
    private final OrderBookRepository orderBookRepository;

    public MarketDataService(ExchangeMarketDataCacheRepository marketDataRepository,
                             OrderBookRepository orderBookRepository) {
        this.marketDataRepository = marketDataRepository;
        this.orderBookRepository = orderBookRepository;
    }

    public ExchangeMarketDataCache findByExchange(Exchange exchange) {
        return marketDataRepository
                .findById(exchange)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("MarketData With ID %s Not Found", exchange)));
    }

    public MarketDataCache findByExchangeAndProduct(Exchange exchange,
                                                    String product) {
        return findByExchange(exchange)
                .getMarketDataCaches()
                .get(product);
    }

    public List<OrderBook> findOpenOrdersByExchangeAndProductAndOrderSide(Exchange exchange,
                                                                          String product,
                                                                          OrderSide side) {
        return orderBookRepository
                .findAllByProductAndExchangeAndOrderSide(product,
                        exchange,
                        side);
    }

    public List<OrderBook> findOpenLimitOrdersByProductAndOrderSide(String product, OrderSide side) {
        return orderBookRepository
                .findAllByProductAndOrderSideAndOrderType(product,
                        side,
                        OrderType.LIMIT);
    }

}
