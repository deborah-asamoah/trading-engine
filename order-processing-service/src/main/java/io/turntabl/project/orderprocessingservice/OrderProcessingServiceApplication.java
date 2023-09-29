package io.turntabl.project.orderprocessingservice;

import io.turntabl.project.marketdatastore.config.RedisConfig;
import io.turntabl.project.reportingcontract.config.KafkaProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EntityScan("io.turntabl.project.persistence.entities")
@EnableJpaRepositories("io.turntabl.project.persistence.repositories")
@Import({RedisConfig.class, KafkaProducerConfig.class})
@EnableRedisRepositories("io.turntabl.project.marketdatastore.repository")
public class OrderProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingServiceApplication.class, args);
	}

}
