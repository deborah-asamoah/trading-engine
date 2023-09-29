package io.turntabl.project.exchangeclient.dtos.requestbodies;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestBody {
    private String product;
    private int quantity;
    private double price;
    private OrderSide side;
    private OrderType type;
}
