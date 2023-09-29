package io.turntabl.project.orderprocessingservice.controllers;


import io.turntabl.project.exchangeclient.dtos.requestbodies.ExchangeWebhookEventRequestBody;
import io.turntabl.project.orderprocessingservice.events.ExchangeWebhookEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange-webhook")
public class ExchangeWebHookController {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ExchangeWebHookController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("events")
    @ResponseStatus(HttpStatus.OK)
    void onEvent(@RequestBody ExchangeWebhookEventRequestBody body) {
        System.out.println(body);
        applicationEventPublisher.publishEvent(new ExchangeWebhookEvent(body));
    }
}
