package io.turntabl.project.orderprocessingservice.factories;

import io.turntabl.project.exchangeclient.ExchangeClient;
import io.turntabl.project.exchangeclient.ExchangeClientConfigParams;
import io.turntabl.project.exchangeclient.enums.Exchange;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExchangeClientFactory {
    private final Map<Exchange, ExchangeClient> exchangeClients;

    @Value("${api.key}")
    private String apiKey;

    public ExchangeClientFactory() {
        this.exchangeClients = new HashMap<>();
    }

    @PostConstruct
    public void postConstruct() {
        loadExchangeClients();
    }

    public void loadExchangeClients() {
        for (var exchange : Exchange.values())
            exchangeClients.put(exchange, new ExchangeClient(ExchangeClientConfigParams
                    .builder()
                    .apiKey(apiKey)
                    .baseUrl(exchange.getUrl())
                    .build()));
    }

    public Collection<ExchangeClient> getExchangeClients() {
        return exchangeClients.values();
    }

    public ExchangeClient getExchangeClientById(Exchange exchange) {
        return exchangeClients.get(exchange);
    }

    public void clearExchangeClients() {
        exchangeClients.clear();
    }

}
