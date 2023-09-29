package io.turntabl.project.orderprocessingapi.dtos;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderDto {
    private UUID id;
    private String product;
    private Long quantity;
    private Double price;
    private OrderSide side;
    private OrderType type;
    private UUID clientId;
}
