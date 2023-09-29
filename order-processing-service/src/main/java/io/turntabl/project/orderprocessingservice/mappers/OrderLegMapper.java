package io.turntabl.project.orderprocessingservice.mappers;

import io.turntabl.project.exchangeclient.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.orderprocessingapi.dtos.OrderLegDto;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderLegMapper {
    OrderLegDto toOrderLegDto(OrderLeg orderLeg);

    @Mapping(source = "order.product", target = "product")
    @Mapping(source = "order.side", target = "side")
    @Mapping(source = "order.type", target = "type")
    CreateOrderRequestBody toCreateOrderRequestBody(OrderLeg orderLeg);

}
