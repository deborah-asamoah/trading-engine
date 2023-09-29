package io.turntabl.project.clientservice.mappers;

import io.turntabl.project.orderprocessingapi.dtos.OrderViewDto;
import io.turntabl.project.persistence.entities.OrderView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderViewDtoMapper {
    OrderViewDto toOrderViewDto(OrderView orderView);
}
