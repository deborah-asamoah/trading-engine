package io.turntabl.project.marketdataservice.mappers;

import io.turntabl.project.exchangeclient.dtos.responsebodies.OrderBookResponseBody;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderBookMapper {

    @Mapping(source = "cumulatitiveQuantity", target = "cumulativeQuantity")
    @Mapping(source = "cumulatitivePrice", target = "cumulativePrice")
    OrderBook toOrderBook(OrderBookResponseBody orderBookResponseBody);

}
