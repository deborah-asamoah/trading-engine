package io.turntabl.project.marketdatastore.entity;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "OrderBookCache")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {
    @Id
    private UUID orderId;
    @Indexed
    private String product;
    private int quantity;
    private double price;
    @Indexed
    private OrderType orderType;
    @Indexed
    private OrderSide orderSide;
    private int cumulativeQuantity;
    private double cumulativePrice;
    @Indexed
    private Exchange exchange;
}
