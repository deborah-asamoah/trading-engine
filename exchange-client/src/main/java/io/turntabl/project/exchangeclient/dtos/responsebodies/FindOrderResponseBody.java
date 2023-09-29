package io.turntabl.project.exchangeclient.dtos.responsebodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.turntabl.project.orderprocessingapi.enums.OrderSide;
import io.turntabl.project.orderprocessingapi.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FindOrderResponseBody {
    private String orderID;
    private String product;
    private int quantity;
    private Double price;
    private OrderSide side;
    private OrderType orderType;
    private int cumulatitiveQuantity;
    private Double cumulatitivePrice;
}
