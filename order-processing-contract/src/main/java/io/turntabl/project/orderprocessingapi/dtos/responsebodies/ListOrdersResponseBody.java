package io.turntabl.project.orderprocessingapi.dtos.responsebodies;

import io.turntabl.project.orderprocessingapi.dtos.OrderViewDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListOrdersResponseBody {
    private List<OrderViewDto> data;
}
