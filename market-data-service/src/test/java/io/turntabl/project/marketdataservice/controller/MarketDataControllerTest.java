package io.turntabl.project.marketdataservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.project.exchangeclient.dtos.inputBodies.MarketDataUpdate;
import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import io.turntabl.project.exchangeclient.enums.Exchange;
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
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarketDataController.class)
class MarketDataControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MarketDataCacheMapper marketDataCacheMapper;
    @MockBean
    private LastProductTradeMapper lastProductTradeMapper;
    @MockBean
    private OrderBookMapper orderBookMapper;
    @MockBean
    private OrderBookService orderBookService;
    @MockBean
    private ExchangeClientService exchangeClientService;
    @MockBean
    private ExchangeMarketDataCacheService exchangeMarketDataCacheService;
    @MockBean
    private LastProductTradeService lastProductTradeService;
    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;
    private MarketDataUpdate marketDataUpdate;

    @BeforeEach
    void setup() {
        List<MarketData> marketDataList = List.of(new MarketData(),
                new MarketData(), new MarketData());
        List<MarketDataCache> marketDataCaches = List.of(
                MarketDataCache.builder().product("IBM").build(),
                MarketDataCache.builder().product("MSFT").build(),
                MarketDataCache.builder().product("GOOGL").build());
        List<LastProductTrade> lastProductTrades = List.of(
                new LastProductTrade(),
                new LastProductTrade(),
                new LastProductTrade()
        );
        List<OrderBookResponseBody> orderBookResponseBodies = List.of(
                new OrderBookResponseBody(),
                new OrderBookResponseBody(),
                new OrderBookResponseBody()
        );
        List<OrderBook> orderBooks = List.of(
                new OrderBook(),
                new OrderBook(),
                new OrderBook()
        );
        marketDataUpdate = MarketDataUpdate.builder()
                .orderSide(OrderSide.BUY)
                .orderId(UUID.fromString("9f820044-fb6f-482c-8a2c-d342b6d39305"))
                .price(1.18)
                .quantity(200)
                .product("GOOGL")
                .orderType(OrderType.LIMIT)
                .cumulativeQuantity(200)
                .cumulativePrice(1.18)
                .exchange(Exchange.MAL2)
                .timestamp(LocalDateTime.parse("2023-05-17T11:40:20.331"))
                .build();

        when(exchangeClientService.getMarketDataUpdate(any(Exchange.class)))
                .thenReturn(marketDataList);
        when(exchangeClientService.getProductOpenOrders(anyString(),any(Exchange.class)))
                .thenReturn(orderBookResponseBodies);
        when(marketDataCacheMapper.toMarketDataCache(any(MarketData.class)))
                .thenReturn(marketDataCaches.get(0))
                .thenReturn(marketDataCaches.get(1))
                .thenReturn(marketDataCaches.get(2));
        when(lastProductTradeMapper.toLastProductTrade(any(MarketData.class)))
                .thenReturn(lastProductTrades.get(0))
                .thenReturn(lastProductTrades.get(1))
                .thenReturn(lastProductTrades.get(2));
        when(orderBookMapper.toOrderBook(any(OrderBookResponseBody.class)))
                .thenReturn(orderBooks.get(0))
                .thenReturn(orderBooks.get(1))
                .thenReturn(orderBooks.get(2));
    }

    @Test
    void receiveMarketUpdateAndQueryForMarketData() throws Exception {
        RequestBuilder request = post("/api/v1/market-data/market-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marketDataUpdate))
                .accept(MediaType.APPLICATION_JSON);

        assertDoesNotThrow(() ->  mockMvc.perform(request).andExpect(status().isOk()));

        verify(exchangeClientService, times(1))
                .getMarketDataUpdate(Exchange.MAL2);
        verify(marketDataCacheMapper, times(3))
                .toMarketDataCache(any(MarketData.class));
        verify(exchangeMarketDataCacheService, times(1))
                .save(any(ExchangeMarketDataCache.class));
        verify(simpMessagingTemplate, times(1))
                .convertAndSend(anyString(), any(ExchangeMarketDataCache.class));
    }

    @Test
    void receiveUpdateAndFilterForLastProductTrade() throws JsonProcessingException {
        RequestBuilder request = post("/api/v1/market-data/market-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marketDataUpdate))
                .accept(MediaType.APPLICATION_JSON);

        assertDoesNotThrow(() ->  mockMvc.perform(request).andExpect(status().isOk()));

        verify(exchangeClientService, times(1))
                .getMarketDataUpdate(Exchange.MAL2);
        verify(lastProductTradeMapper, times(3))
                .toLastProductTrade(any(MarketData.class));
        verify(lastProductTradeService, times(1))
                .saveAll(anyList());
    }

    @Test
    void receiveUpdateAndGetOrderBookForProduct() throws JsonProcessingException {
        RequestBuilder request = post("/api/v1/market-data/market-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marketDataUpdate))
                .accept(MediaType.APPLICATION_JSON);

        assertDoesNotThrow(() ->  mockMvc.perform(request).andExpect(status().isOk()));

        verify(exchangeClientService, times(1))
                .getProductOpenOrders("GOOGL",Exchange.MAL2);
        verify(orderBookMapper, times(3))
                .toOrderBook(any(OrderBookResponseBody.class));
        verify(orderBookService, times(1))
                .saveAll(anyList());
        verify(orderBookService, times(1))
                .deleteByProductAndExchange(anyString(),any(Exchange.class));
        verify(simpMessagingTemplate, times(1))
                .convertAndSend(anyString(), any(OrderBooksDto.class));
    }

    @Test
    void receiveUpdateReturnsExceptionMessageOnEmptyBody() throws Exception {
        RequestBuilder request = post("/api/v1/market-data/market-update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Body is required"))
                .andExpect(jsonPath("$.error").value("Bad request"));
    }
}