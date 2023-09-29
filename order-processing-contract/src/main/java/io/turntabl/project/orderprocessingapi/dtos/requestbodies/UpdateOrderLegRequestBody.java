package io.turntabl.project.orderprocessingapi.dtos.requestbodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderLegRequestBody {
    private Integer quantity;
    private Double price;
}
