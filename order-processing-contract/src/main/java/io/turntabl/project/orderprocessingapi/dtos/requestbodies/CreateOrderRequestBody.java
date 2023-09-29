package io.turntabl.project.orderprocessingapi.dtos.requestbodies;

import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateOrderRequestBody {
    private String product;
    private long quantity;
    private double price;
    private OrderSide side;
    private OrderType type;
    private UUID portfolioId;
}

