package io.turntabl.project.orderprocessingapi.dtos;

import java.util.UUID;

public class OrderLegDto {
    private UUID id;
    private long quantity;
    private double price;
    private String exchangeOrderId;
    private long cumulativeQuantity;
    private Double cumulativePrice;
    private boolean cancelled;
}
