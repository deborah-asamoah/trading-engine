package io.turntabl.project.marketdataservice.dto;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderBooksDto {
    private List<OrderBook> orderBookList;
    private String product;
    private Exchange exchange;
}
