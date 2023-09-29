package io.turntabl.project.persistence.entities;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Table(name = "order_view")
@Immutable
@Getter
@Setter
public class OrderView {
    @Id
    private UUID id;
    private String product;
    private int quantity;
    private double price;
    @Column(name = "order_side")
    private OrderSide side;
    @Column(name = "order_type")
    private OrderType type;
    private UUID portfolio;
    @Column(name = "is_cancelled")
    private boolean cancelled;
    @Column(name = "client_name")
    private String clientName;
    private UUID client;
    @Column(name = "cum_quantity")
    private int cumulativeQuantity;
    private Double value;
    @Column(name = "is_complete")
    private boolean complete;
}
