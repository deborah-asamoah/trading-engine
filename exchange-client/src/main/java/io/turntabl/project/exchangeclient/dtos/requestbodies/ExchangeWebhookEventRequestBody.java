package io.turntabl.project.exchangeclient.dtos.requestbodies;


import io.turntabl.project.exchangeclient.enums.Exchange;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExchangeWebhookEventRequestBody {
    private String orderType;
    private String product;
    private String side;
    private String orderID;
    private Double price;
    private int qty;
    private int cumQty;
    private Double cumPrx;
    private Exchange exchange;
    private String timestamp;

}
