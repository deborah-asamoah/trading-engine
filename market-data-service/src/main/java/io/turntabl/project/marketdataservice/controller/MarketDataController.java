package io.turntabl.project.marketdataservice.controller;

import io.turntabl.project.exchangeclient.dtos.inputBodies.MarketDataUpdate;
import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdataservice.dto.LastProductTradeUpdateDto;
import io.turntabl.project.marketdataservice.dto.OrderBooksDto;
import io.turntabl.project.marketdataservice.mappers.LastProductTradeMapper;
import io.turntabl.project.marketdataservice.mappers.MarketDataCacheMapper;
import io.turntabl.project.marketdataservice.mappers.OrderBookMapper;
import io.turntabl.project.marketdataservice.service.ExchangeClientService;
import io.turntabl.project.marketdataservice.service.ExchangeMarketDataCacheService;
import io.turntabl.project.marketdataservice.service.LastProductTradeService;
import io.turntabl.project.marketdataservice.service.OrderBookService;
import io.turntabl.project.marketdatastore.entity.ExchangeMarketDataCache;
import io.turntabl.project.marketdatastore.entity.LastProductTrade;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/market-data")
public class MarketDataController {
    @Autowired
    private MarketDataCacheMapper marketDataCacheMapper;
    @Autowired
    private LastProductTradeMapper lastProductTradeMapper;
    @Autowired
    private OrderBookMapper orderBookMapper;
    @Autowired
    private ExchangeClientService exchangeClientService;
    @Autowired
    private ExchangeMarketDataCacheService exchangeMarketDataCacheService;
    @Autowired
    private LastProductTradeService lastProductTradeService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private OrderBookService orderBookService;

    @PostMapping("/market-update")
    @ResponseStatus(HttpStatus.OK)
    public void receiveUpdate(@RequestBody MarketDataUpdate update) {
        Exchange exchange = update.getExchange();
        String product = update.getProduct();
        List<MarketData> marketDataList = exchangeClientService.getMarketDataUpdate(exchange);
        List<OrderBookResponseBody> orderBookResponseBodyList = exchangeClientService.getProductOpenOrders(product, exchange);
        handleMarketDataCache(marketDataList, exchange);
        handleLastProductTrade(marketDataList, exchange);
        handleProductOpenOrders(orderBookResponseBodyList, exchange, product);
    }

    private void handleProductOpenOrders(List<OrderBookResponseBody> responseBodies, Exchange exchange, String product) {
        orderBookService.deleteByProductAndExchange(product,exchange);
        List<OrderBook> orderBooks = responseBodies.stream().map((book) -> {
            OrderBook orderBook = orderBookMapper.toOrderBook(book);
            orderBook.setExchange(exchange);
            return orderBook;
        }).toList();
        orderBookService.saveAll(orderBooks);
        sendUpdateToClient("/order-book/update",new OrderBooksDto(orderBooks,product,exchange));
    }

    private void handleMarketDataCache(List<MarketData> marketDataList, Exchange exchange) {
        Map<String,MarketDataCache> marketDataCaches = marketDataList
                .stream()
                .map((marketData) -> marketDataCacheMapper.toMarketDataCache(marketData))
                .collect(Collectors.toMap(MarketDataCache::getProduct, Function.identity()));
        ExchangeMarketDataCache exchangeMarketDataCache = new ExchangeMarketDataCache(
                exchange,
                marketDataCaches
        );
        sendUpdateToClient("/market-data/update",exchangeMarketDataCache);
        exchangeMarketDataCacheService.save(exchangeMarketDataCache);
    }

    private void handleLastProductTrade(List<MarketData> marketDataList, Exchange exchange) {
        LocalDateTime time = LocalDateTime.now();
        List<LastProductTrade> lastProductTrades = marketDataList.stream().map((marketData) -> {
            LastProductTrade lastProductTrade = lastProductTradeMapper.toLastProductTrade(marketData);
            lastProductTrade.setExchange(exchange);
            lastProductTrade.setTimestamp(time);
            return lastProductTrade;
        }).toList();
        lastProductTradeService.saveAll(lastProductTrades);
        sendUpdateToClient("/trend/update",new LastProductTradeUpdateDto(exchange,lastProductTrades));
    }

    @PostConstruct
    private void getMarketDataOnStartUp() {
        Exchange exchange = Exchange.MAL1;
        List<MarketData> marketDataList = exchangeClientService.getMarketDataUpdate(exchange);
        handleMarketDataCache(marketDataList, exchange);
        handleLastProductTrade(marketDataList, exchange);

        for (var marketData : marketDataList)
            handleProductOpenOrders(exchangeClientService
                            .getProductOpenOrders(marketData.getProduct(), exchange),
                    exchange,
                    marketData.getProduct());

        exchange = Exchange.MAL2;
        marketDataList = exchangeClientService.getMarketDataUpdate(exchange);
        handleMarketDataCache(marketDataList, exchange);
        handleLastProductTrade(marketDataList, exchange);

        for (var marketData : marketDataList)
            handleProductOpenOrders(exchangeClientService
                            .getProductOpenOrders(marketData.getProduct(), exchange),
                    exchange,
                    marketData.getProduct());
    }

    @MessageMapping("/market-data/initial")
    @SendTo("/market-data/update")
    public List<ExchangeMarketDataCache> onInitialData() {
        return exchangeMarketDataCacheService.getAll();
    }

    @MessageMapping("/trend/initial")
    @SendTo("/trend/update")
    public Map<Exchange,Map<String, List<LastProductTrade>>> getMarketTrend() {
        Map<Exchange,Map<String, List<LastProductTrade>>> trendMap = new HashMap<>();
        lastProductTradeService.getTrades().forEach((trade) -> {
            if(trade != null) {
                Map<String, List<LastProductTrade>> productTradesMap = trendMap.getOrDefault(trade.getExchange(),new HashMap<>());
                List<LastProductTrade> tradesList = productTradesMap.getOrDefault(trade.getProduct(),new LinkedList<>());
                tradesList.add(trade);
                productTradesMap.put(trade.getProduct(),tradesList);
                trendMap.put(trade.getExchange(),productTradesMap);
            }
        });
        return trendMap;
    }

    @MessageMapping("/order-book/initial")
    @SendTo("/order-book/update")
    public List<OrderBooksDto> onInitialOrderBook() {
        Map<String,OrderBooksDto> orderBooksDtoMap = new HashMap<>();
        orderBookService.getAll().forEach((orderBook) -> {
            String product = orderBook.getProduct();
            OrderBooksDto orderBooksDto = orderBooksDtoMap
                    .getOrDefault(product,new OrderBooksDto(new LinkedList<>(),product,null));
            orderBooksDto.getOrderBookList().add(orderBook);
            orderBooksDtoMap.put(product,orderBooksDto);
        });
        return orderBooksDtoMap.values().stream().toList();
    }

    private void sendUpdateToClient(String url, Object data) {
        simpMessagingTemplate.convertAndSend(url, data);
    }
}
