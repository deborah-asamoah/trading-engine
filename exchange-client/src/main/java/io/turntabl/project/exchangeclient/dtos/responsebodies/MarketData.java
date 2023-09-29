package io.turntabl.project.exchangeclient.dtos.responsebodies;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarketData {
    @JsonAlias({"BID_PRICE"})
    private Double bidPrice;
    @JsonAlias({"SELL_LIMIT"})
    private Integer sellLimit;
    @JsonAlias({"ASK_PRICE"})
    private Double askPrice;
    @JsonAlias({"BUY_LIMIT"})
    private Integer buyLimit;
    @JsonAlias({"MAX_PRICE_SHIFT"})
    private Double maxPriceShift;
    @JsonAlias({"TICKER"})
    private String product;
    @JsonAlias({"LAST_TRADED_PRICE"})
    private Double lastTradedPrice;
}