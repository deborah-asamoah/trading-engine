package io.turntabl.project.orderprocessingapi.dtos.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderCreatedResponseBody {
    private String orderId;
}
