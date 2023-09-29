package io.turntabl.project.marketdatastore.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketDataCache implements Serializable {
    private String product;
    /**
     * Price at which somebody wants to buy
     */
    private double bidPrice;
    private int sellLimit;
    /**
     * Price at which somebody is willing to sell
     */
    private double askPrice;
    private int buyLimit;
    /**
     * The highest my price can differ from the bid price
     * cannot bid beyong maxPriceShift + bidPrice
     * can ask around  askPrice (+/-) maxPriceShift
     */
    private double maxPriceShift;
    private double lastTradedPrice;
}
