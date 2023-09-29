package io.turntabl.project.persistence.entities;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "trade_order")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String product;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double price;
    @Column(name = "order_side",
            nullable = false)
    private OrderSide side;
    @Column(name = "order_type",
            nullable = false)
    private OrderType type;
    @ManyToOne
    @JoinColumn(name = "portfolio",
            nullable = false)
    private Portfolio portfolio;
    @Column(name = "is_cancelled",
            nullable = false)
    private boolean cancelled;
}
