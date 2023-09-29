package io.turntabl.project.exchangeclient;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.exchangeclient.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.exchangeclient.dtos.requestbodies.UpdateOrderRequestBody;
import io.turntabl.project.exchangeclient.dtos.responsebodies.ExceptionResponseBody;
import io.turntabl.project.exchangeclient.dtos.responsebodies.FindOrderResponseBody;
import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ExchangeClient {
    private final ExchangeProxyInterface exchangeProxyInterface;
    private final String apiKey;
    private final JsonMapper jsonMapper;

    public ExchangeClient(ExchangeClientConfigParams configParams) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager
                .setMaxTotal(Optional
                        .ofNullable(configParams.getMaxTotalConnections())
                        .orElse(200));
        connectionManager
                .setDefaultMaxPerRoute(Optional
                        .ofNullable(configParams.getDefaultMaxPerRoute())
                        .orElse(20));

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
        ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(httpClient);

        ResteasyClient client = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
                .httpEngine(engine)
                .build();
        ResteasyWebTarget target = client
                .target(configParams.getBaseUrl());

        exchangeProxyInterface = target.proxy(ExchangeProxyInterface.class);
        apiKey = configParams.getApiKey();
        jsonMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public String createOrder(CreateOrderRequestBody body) {
        try {
            return exchangeProxyInterface.createOrder(apiKey, body);
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    public FindOrderResponseBody getOrderById(String id) {
        try {
            return exchangeProxyInterface.getOrderById(apiKey, id);
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    public List<MarketData> getMarketData() {
        try {
            return exchangeProxyInterface.getMarketData();
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    public List<OrderBookResponseBody> getProductOpenOrders(String product) {
        try {
            return exchangeProxyInterface.getProductOpenOrders(product);
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    public boolean cancelOrder(String id) {
        try {
            return exchangeProxyInterface.cancelOrderById(apiKey, id);
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    public boolean updateOrder(String id, UpdateOrderRequestBody body) {
        try {
            return exchangeProxyInterface.updateOrderById(apiKey, id, body);
        } catch (Exception e) {
            handleExchangeException(e);
            throw new RuntimeException(e);
        }
    }

    private void handleExchangeException(Exception exception) {
        if (exception instanceof WebApplicationException e) {
            try {
                ExceptionResponseBody exceptionResponseBody = jsonMapper
                        .readValue((ByteArrayInputStream) e
                                .getResponse()
                                .getEntity(), ExceptionResponseBody.class);
                throw new ExchangeException(exceptionResponseBody.getMessage(),
                        exceptionResponseBody,
                        e);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
