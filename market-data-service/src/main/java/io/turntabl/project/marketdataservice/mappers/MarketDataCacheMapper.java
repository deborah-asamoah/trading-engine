package io.turntabl.project.marketdataservice.mappers;

import io.turntabl.project.exchangeclient.dtos.responsebodies.MarketData;
import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarketDataCacheMapper {
    MarketDataCache toMarketDataCache(MarketData marketData);
}