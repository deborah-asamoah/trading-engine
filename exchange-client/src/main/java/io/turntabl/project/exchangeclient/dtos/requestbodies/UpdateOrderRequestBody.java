package io.turntabl.project.exchangeclient.dtos.requestbodies;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestBody {
    private String product;
    private int quantity;
    private double price;
    private OrderSide side;
    private OrderType type;
}
