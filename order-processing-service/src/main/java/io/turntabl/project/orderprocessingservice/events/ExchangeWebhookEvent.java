package io.turntabl.project.orderprocessingservice.events;

import io.turntabl.project.exchangeclient.dtos.requestbodies.ExchangeWebhookEventRequestBody;

public record ExchangeWebhookEvent(ExchangeWebhookEventRequestBody exchangeWebhookEventRequestBody) {
}
