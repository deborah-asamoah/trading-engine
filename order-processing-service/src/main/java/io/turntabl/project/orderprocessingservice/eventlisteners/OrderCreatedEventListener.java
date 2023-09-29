package io.turntabl.project.orderprocessingservice.eventlisteners;

import io.turntabl.project.orderprocessingservice.events.OrderCreatedEvent;
import io.turntabl.project.orderprocessingservice.models.TradingParameters;
import io.turntabl.project.orderprocessingservice.services.OrderLegService;
import io.turntabl.project.orderprocessingservice.services.TradingStrategiesService;
import io.turntabl.project.orderprocessingservice.utils.OrderLoggingUtils;
import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderLeg;
import io.turntabl.project.reportingcontract.enums.OrderEventType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Queue;

@Component
public class OrderCreatedEventListener {

    private final TradingStrategiesService tradingStrategiesService;
    private final OrderLegService orderLegService;
    private final OrderLoggingUtils orderLoggingUtils;

    public OrderCreatedEventListener(TradingStrategiesService tradingStrategiesService,
                                     OrderLegService orderLegService,
                                     OrderLoggingUtils orderLoggingUtils) {
        this.tradingStrategiesService = tradingStrategiesService;
        this.orderLegService = orderLegService;
        this.orderLoggingUtils = orderLoggingUtils;
    }

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        Order order = event.order();

        Queue<TradingParameters> tradingParameters = tradingStrategiesService.compileTradingParameters(order);

        Queue<OrderLeg> orderLegs = tradingStrategiesService.compileOrderLegs(order, tradingParameters);

        for (var orderLeg : orderLegs) {
            try {
                orderLegService.createOrderLeg(orderLeg);
                orderLoggingUtils.logEvent(OrderEventType.CREATE_ORDER,
                        order.getId(),
                        "ORDER LEG FOR " + order.getId() + " CREATED AT " + orderLeg.getExchange());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
