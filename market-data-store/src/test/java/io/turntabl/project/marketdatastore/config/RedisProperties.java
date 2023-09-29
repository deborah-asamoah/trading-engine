package io.turntabl.project.marketdatastore.config;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@Getter
public class RedisProperties {
    private final int redisPort;
    private final String redisHost;

    public RedisProperties() {
        this.redisPort = 6370;
        this.redisHost = "localhost";
    }
}
