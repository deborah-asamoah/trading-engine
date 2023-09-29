package io.turntabl.project.marketdatastore.entity;

import io.turntabl.project.exchangeclient.enums.Exchange;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

@RedisHash(value = "ExchangeMarketDataCache")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeMarketDataCache implements Serializable {
    @Id
    private Exchange exchange;
    private Map<String,MarketDataCache> marketDataCaches;
}
