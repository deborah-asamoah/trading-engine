package io.turntabl.project.reportingcontract.entities;

import io.turntabl.project.reportingcontract.enums.OrderEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Table(name = "order_report")
@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class OrderEvent extends Event {
    @Column(nullable = false)
    private OrderEventType type;
    @Column(nullable = false)
    private UUID orderId;
}
