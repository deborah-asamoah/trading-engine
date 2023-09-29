package io.turntabl.project.orderprocessingservice.events;

import io.turntabl.project.persistence.entities.Order;

public record OrderCancelledEvent(Order order) {
}
