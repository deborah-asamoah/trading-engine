package io.turntabl.project.marketdatastore.repository;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.ExchangeMarketDataCache;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeMarketDataCacheRepository extends CrudRepository<ExchangeMarketDataCache, Exchange> {
}
