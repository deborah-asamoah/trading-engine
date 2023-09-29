package io.turntabl.project.orderprocessingservice.eventlisteners;

import io.turntabl.project.orderprocessingservice.events.OrderLegUpdatedEvent;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.springframework.context.event.EventListener;

public class OrderLegUpdatedListener {
    private OrderLeg orderLeg;

    public OrderLegUpdatedListener(OrderLeg orderLeg) {
        this.orderLeg = orderLeg;
    }

    @EventListener
    public void onOrderLegUpdated (OrderLegUpdatedEvent orderLegUpdatedEvent) {
        var update = orderLegUpdatedEvent.orderLeg();








    }
}
