package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.reportingcontract.entities.OrderEvent;
import io.turntabl.project.reportingcontract.enums.OrderEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OrderLoggingUtils {
    @Value("${topic.order-log}")
    private String orderLogTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderLoggingUtils(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logEvent(OrderEventType event, UUID id, String description) {
        OrderEvent orderEvent = OrderEvent
                .builder()
                .orderId(id)
                .type(event)
                .happenedOn(LocalDateTime.now())
                .description(description)
                .build();
        kafkaTemplate.send(orderLogTopic,orderEvent);
    }
}
