package io.turntabl.project.exchangeclient.dtos.responsebodies;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class OrderBookResponseBody {
    @JsonAlias({"orderID"})
    private UUID orderId;
    private String product;
    private int quantity;
    private double price;
    private OrderType orderType;
    @JsonAlias({"side"})
    private OrderSide orderSide;
    private int cumulatitiveQuantity;
    private double cumulatitivePrice;
}
