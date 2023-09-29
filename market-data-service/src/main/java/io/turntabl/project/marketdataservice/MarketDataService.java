package io.turntabl.project.marketdataservice;

import io.turntabl.project.marketdatastore.config.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@Import({ RedisConfig.class })
@EntityScan("io.turntabl.project.marketdatastore.entity")
@EnableRedisRepositories("io.turntabl.project.marketdatastore.repository")
public class MarketDataService {
    public static void main(String[] args) {
        SpringApplication.run(MarketDataService.class, args);
    }
}