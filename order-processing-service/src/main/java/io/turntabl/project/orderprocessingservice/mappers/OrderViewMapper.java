package io.turntabl.project.orderprocessingservice.mappers;

import io.turntabl.project.orderprocessingapi.dtos.OrderViewDto;
import io.turntabl.project.persistence.entities.OrderView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderViewMapper {
    OrderViewDto toOrderViewDto(OrderView orderView);

}
