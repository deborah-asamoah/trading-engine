package io.turntabl.project.clientservice.utils;

import io.turntabl.project.reportingcontract.entities.ClientEvent;
import io.turntabl.project.reportingcontract.enums.ClientEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ClientLoggingUtils {
    @Value("${topic.client-log}")
    private String clientLogTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ClientLoggingUtils(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logEvent(ClientEventType event, UUID id, String description) {
        ClientEvent orderEvent = ClientEvent
                .builder()
                .clientId(id)
                .type(event)
                .happenedOn(LocalDateTime.now())
                .description(description)
                .build();
        kafkaTemplate.send(clientLogTopic,orderEvent);
    }
}
