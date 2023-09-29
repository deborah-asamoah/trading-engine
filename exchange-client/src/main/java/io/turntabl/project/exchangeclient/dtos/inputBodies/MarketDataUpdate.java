package io.turntabl.project.exchangeclient.dtos.inputBodies;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataUpdate {
    private OrderType orderType;
    private String product;
    private OrderSide orderSide;
    @JsonAlias({"orderID"})
    private UUID orderId;
    @JsonAlias({"qty"})
    private Integer quantity;
    private Double price;
    @JsonAlias({"cumQty"})
    private Integer cumulativeQuantity;
    @JsonAlias({"cumPrx"})
    private Double cumulativePrice;
    private Exchange exchange;
    private LocalDateTime timestamp;
}