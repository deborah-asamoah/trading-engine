package io.turntabl.project.reportingservice.listeners;

import io.turntabl.project.reportingcontract.entities.ClientEvent;
import io.turntabl.project.reportingcontract.entities.OrderEvent;
import io.turntabl.project.reportingservice.services.ClientEventService;
import io.turntabl.project.reportingservice.services.OrderEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventListener {
    private static final String ORDER_GROUP_ID = "order-log";
    private static final String CLIENT_GROUP_ID = "client-log";
    private static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    private static final String ORDER_LOG_TOPIC = "order-log-topic";
    private static final String CLIENT_LOG_TOPIC = "client-log-topic";

    private final OrderEventService orderEventService;
    private final ClientEventService clientEventService;

    public LoggingEventListener(OrderEventService orderEventService,
                                ClientEventService clientEventService) {
        this.orderEventService = orderEventService;
        this.clientEventService = clientEventService;
    }

    @KafkaListener(topics = ORDER_LOG_TOPIC, groupId = ORDER_GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void orderLogListener(OrderEvent event) {
        orderEventService.save(event);
    }

    @KafkaListener(topics = CLIENT_LOG_TOPIC, groupId = CLIENT_GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void clientLogListener(ClientEvent event) {
        clientEventService.save(event);
    }
}
