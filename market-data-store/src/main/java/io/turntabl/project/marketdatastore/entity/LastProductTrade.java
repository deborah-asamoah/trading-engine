package io.turntabl.project.marketdatastore.entity;

import io.turntabl.project.exchangeclient.enums.Exchange;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash(value = "LastProductTrade", timeToLive = 7200)
@Getter
@Setter
@ToString
public class LastProductTrade implements Serializable {
    @Id
    private String id;
    private Double lastTradedPrice;
    private String product;
    private Exchange exchange;
    private LocalDateTime timestamp;
}
