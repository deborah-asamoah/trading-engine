package io.turntabl.project.orderprocessingservice.mappers;

import io.turntabl.project.orderprocessingapi.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.persistence.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateOrderRequestBodyMapper {
    Order toOrder(CreateOrderRequestBody createOrderRequestBody);
}
