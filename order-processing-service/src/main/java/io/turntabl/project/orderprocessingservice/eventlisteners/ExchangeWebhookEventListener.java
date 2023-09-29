package io.turntabl.project.orderprocessingservice.eventlisteners;

import io.turntabl.project.exchangeclient.ExchangeClient;
import io.turntabl.project.orderprocessingservice.events.ExchangeWebhookEvent;
import io.turntabl.project.orderprocessingservice.events.OrderLegUpdatedEvent;
import io.turntabl.project.orderprocessingservice.factories.ExchangeClientFactory;
import io.turntabl.project.orderprocessingservice.services.OrderLegService;
import io.turntabl.project.persistence.entities.OrderLeg;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ExchangeWebhookEventListener {

    private final OrderLegService orderLegService;
    private final ExchangeClientFactory exchangeClientFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ExchangeWebhookEventListener(OrderLegService orderLegService,
                                        ExchangeClientFactory exchangeClientFactory,
                                        ApplicationEventPublisher applicationEventPublisher) {
        this.orderLegService = orderLegService;
        this.exchangeClientFactory = exchangeClientFactory;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @EventListener
    public void onExchangeWebhookEvent(ExchangeWebhookEvent event) {
        var update = event.exchangeWebhookEventRequestBody();

        OrderLeg orderLeg = orderLegService
                .findByExchangeOrderId(update.getOrderID());

        ExchangeClient exchangeClient = exchangeClientFactory
                .getExchangeClientById(update.getExchange());

        var details = exchangeClient.getOrderById(update.getOrderID());

        orderLeg.setCumulativeQuantity(details.getCumulatitiveQuantity());
        orderLeg.setCumulativePrice(details.getCumulatitivePrice());

        applicationEventPublisher.publishEvent(new OrderLegUpdatedEvent(orderLeg));
    }

}
