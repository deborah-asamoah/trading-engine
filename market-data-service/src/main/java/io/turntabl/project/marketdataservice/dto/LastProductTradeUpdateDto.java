package io.turntabl.project.marketdataservice.dto;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.LastProductTrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LastProductTradeUpdateDto {
    private Exchange exchange;
    private List<LastProductTrade> lastProductTrades;
}
