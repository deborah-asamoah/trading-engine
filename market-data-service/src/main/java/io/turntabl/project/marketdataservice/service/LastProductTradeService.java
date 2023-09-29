package io.turntabl.project.marketdataservice.service;

import io.turntabl.project.marketdatastore.entity.LastProductTrade;
import io.turntabl.project.marketdatastore.repository.LastProductTradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LastProductTradeService {
    private final LastProductTradeRepository lastProductTradeRepository;

    public LastProductTradeService(LastProductTradeRepository lastProductTradeRepository) {
        this.lastProductTradeRepository = lastProductTradeRepository;
    }

    public void saveAll(List<LastProductTrade> lastProductTrades) {
        lastProductTradeRepository.saveAll(lastProductTrades);
    }

    public Iterable<LastProductTrade> getTrades() {
        return lastProductTradeRepository.findAll();
    }
}
