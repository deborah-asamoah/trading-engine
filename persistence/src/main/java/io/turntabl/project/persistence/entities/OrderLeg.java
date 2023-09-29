package io.turntabl.project.persistence.entities;

import io.turntabl.project.exchangeclient.enums.Exchange;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderLeg {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "trade_order",
            nullable = false)
    private Order order;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false,
            name = "exchange_order_id")
    private String exchangeOrderId;
    private Exchange exchange;
    @Column(nullable = false,
            name = "cum_quantity")
    private int cumulativeQuantity;
    @Column(name = "cum_price")
    private Double cumulativePrice;
    @Column(name = "is_cancelled",
            nullable = false)
    private boolean cancelled;

    public int getBalance() {
        return quantity - cumulativeQuantity;
    }
}
