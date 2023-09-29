package io.turntabl.project.orderprocessingservice.mappers;


import io.turntabl.project.exchangeclient.dtos.requestbodies.CreateOrderRequestBody;
import io.turntabl.project.persistence.entities.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ExchangeClientCreateOrderRequestBodyMapper {
    CreateOrderRequestBody toExchangeClientOrder(Order order);
}
