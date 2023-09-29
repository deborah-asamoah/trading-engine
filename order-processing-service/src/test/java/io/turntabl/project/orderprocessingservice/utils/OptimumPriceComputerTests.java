package io.turntabl.project.orderprocessingservice.utils;

import io.turntabl.project.marketdatastore.entity.MarketDataCache;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimumPriceComputerTests {

    @Test
    void Given_MaxPriceShift_When_1_Then_Use10PercentOfIt() {
        double maxPriceShift = OptimumPriceComputer
                .resolveMaxPriceShift(MarketDataCache
                        .builder()
                        .maxPriceShift(1)
                        .lastTradedPrice(2)
                        .build());

        assertThat(maxPriceShift).isEqualTo(0.2);
    }

    @Test
    void Given_MaxPriceShift_When_Not1_Then_UseAsIs() {
        double maxPriceShift = OptimumPriceComputer
                .resolveMaxPriceShift(MarketDataCache
                        .builder()
                        .maxPriceShift(2)
                        .lastTradedPrice(2)
                        .build());

        assertThat(maxPriceShift).isEqualTo(2);
    }

}
