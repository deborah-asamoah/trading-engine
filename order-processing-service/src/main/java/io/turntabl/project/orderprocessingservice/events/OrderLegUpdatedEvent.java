package io.turntabl.project.orderprocessingservice.events;

import io.turntabl.project.persistence.entities.OrderLeg;

public record OrderLegUpdatedEvent(OrderLeg orderLeg) {
}
