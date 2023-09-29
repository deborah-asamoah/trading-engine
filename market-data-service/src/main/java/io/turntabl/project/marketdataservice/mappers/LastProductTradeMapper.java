package io.turntabl.project.marketdataservice.mappers;

import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.marketdatastore.entity.LastProductTrade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LastProductTradeMapper {
    LastProductTrade toLastProductTrade(MarketData marketData);
}
