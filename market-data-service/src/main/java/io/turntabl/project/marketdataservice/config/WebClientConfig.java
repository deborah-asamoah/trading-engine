package io.turntabl.project.marketdataservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebClientConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${ws.market-data.url}")
    private String marketDataUrl;
    @Value("${ws.order-book.url}")
    private String orderBookUrl;
    @Value("${ws.trend.url}")
    private String trendUrl;
    @Value("${ws.subscribe.url}")
    private String subscribeUrl;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(marketDataUrl,orderBookUrl,trendUrl);
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(subscribeUrl).setAllowedOrigins("http://localhost:4200");
        registry.addEndpoint(subscribeUrl).setAllowedOrigins("http://localhost:4200").withSockJS();
    }
}
