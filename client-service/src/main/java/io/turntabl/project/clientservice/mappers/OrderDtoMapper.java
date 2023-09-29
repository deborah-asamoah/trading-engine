package io.turntabl.project.clientservice.mappers;

import io.turntabl.project.orderprocessingapi.dtos.OrderDto;
import io.turntabl.project.persistence.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {
    OrderDto toOrderDto(Order order);
}
