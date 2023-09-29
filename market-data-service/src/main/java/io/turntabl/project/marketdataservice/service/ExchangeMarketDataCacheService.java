package io.turntabl.project.marketdataservice.service;

import io.turntabl.project.marketdatastore.entity.ExchangeMarketDataCache;
import io.turntabl.project.marketdatastore.repository.ExchangeMarketDataCacheRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeMarketDataCacheService {
    private final ExchangeMarketDataCacheRepository exchangeMarketDataCacheRepository;

    public ExchangeMarketDataCacheService(ExchangeMarketDataCacheRepository marketDataCacheRepository) {
        this.exchangeMarketDataCacheRepository = marketDataCacheRepository;
    }

    public List<ExchangeMarketDataCache> getAll() {
        List<ExchangeMarketDataCache> marketDataCaches = new ArrayList<>();
        exchangeMarketDataCacheRepository.findAll().forEach(marketDataCaches::add);
        return marketDataCaches;
    }

    public void save(ExchangeMarketDataCache exchangeMarketDataCache) {
        exchangeMarketDataCacheRepository.save(exchangeMarketDataCache);
    }
}
