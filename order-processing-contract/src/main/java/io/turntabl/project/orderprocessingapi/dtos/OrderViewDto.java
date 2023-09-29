package io.turntabl.project.orderprocessingapi.dtos;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderViewDto {
    private UUID id;
    private String product;
    private int quantity;
    private double price;
    private OrderSide side;
    private OrderType type;
    private UUID portfolio;
    private boolean cancelled;
    private UUID client;
    private int cumulativeQuantity;
    private String clientName;
    private Double value;
    private boolean complete;
}
