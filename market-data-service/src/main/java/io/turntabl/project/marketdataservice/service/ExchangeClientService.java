package io.turntabl.project.marketdataservice.service;

import io.turntabl.project.exchangeclient.ExchangeClient;
import io.turntabl.project.exchangeclient.ExchangeClientConfigParams;
import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import io.turntabl.project.exchangeclient.enums.Exchange;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Setter
public class ExchangeClientService {
    private Map<Exchange, ExchangeClient> exchangeClientMap;

    @Value("${api.key}")
    private String apiKey;

    @PostConstruct
    public void setUpExchangeClients() {
        this.exchangeClientMap = new HashMap<>();
        for(var exchange: Exchange.values()) {
            exchangeClientMap.put(exchange,new ExchangeClient(ExchangeClientConfigParams
                    .builder()
                    .apiKey(apiKey)
                    .baseUrl(exchange.getUrl())
                    .build())
            );
        }
    }

    public Map<Exchange, ExchangeClient> getExchangeClientMap() {
        return exchangeClientMap;
    }

    public List<MarketData> getMarketDataUpdate(Exchange exchange) {
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        return exchangeClient.getMarketData();
    }

    public List<OrderBookResponseBody> getProductOpenOrders(String product, Exchange exchange) {
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        return exchangeClient.getProductOpenOrders(product);
    }
}
