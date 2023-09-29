package io.turntabl.project.orderprocessingservice.eventlisteners;

import io.turntabl.project.orderprocessingservice.events.OrderCancelledEvent;
import io.turntabl.project.orderprocessingservice.factories.ExchangeClientFactory;
import io.turntabl.project.orderprocessingservice.utils.OrderLoggingUtils;
import io.turntabl.project.persistence.entities.OrderLeg;
import io.turntabl.project.persistence.repositories.OrderLegRepository;
import io.turntabl.project.reportingcontract.enums.OrderEventType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelledEventListener {
    private final OrderLegRepository orderLegRepository;
    private final ExchangeClientFactory exchangeClientFactory;
    private final OrderLoggingUtils orderLoggingUtils;

    public OrderCancelledEventListener(OrderLegRepository orderLegRepository,
                                       ExchangeClientFactory exchangeClientFactory,
                                       OrderLoggingUtils orderLoggingUtils) {
        this.orderLegRepository = orderLegRepository;
        this.exchangeClientFactory = exchangeClientFactory;
        this.orderLoggingUtils = orderLoggingUtils;
    }

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        for (OrderLeg orderLeg : orderLegRepository.findByOrder(event.order())) {
            if (orderLeg.getCumulativeQuantity() < orderLeg.getQuantity()) {
                boolean isCancelled = exchangeClientFactory
                        .getExchangeClientById(orderLeg.getExchange())
                        .cancelOrder(orderLeg.getExchangeOrderId());

                if (isCancelled) {
                    orderLeg.setCancelled(true);
                    orderLegRepository.save(orderLeg);
                    orderLoggingUtils.logEvent(OrderEventType.CANCELED,
                            orderLeg.getId(),"ORDER LEG " + orderLeg.getId() + " CANCELLED");
                }
            }
        }
    }
}
