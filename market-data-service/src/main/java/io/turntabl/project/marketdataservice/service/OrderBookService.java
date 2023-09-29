package io.turntabl.project.marketdataservice.service;

import io.turntabl.project.exchangeclient.enums.Exchange;
import io.turntabl.project.marketdatastore.entity.OrderBook;
import io.turntabl.project.marketdatastore.repository.OrderBookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBookService {
    private final OrderBookRepository orderBookRepository;

    public OrderBookService(OrderBookRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }

    public void deleteByProductAndExchange(String product, Exchange exchange) {
        List<OrderBook> result = orderBookRepository.findAllByProductAndExchange(product,exchange);
        orderBookRepository.deleteAll(result);
    }

    public Iterable<OrderBook> getAll() {
        return orderBookRepository.findAll();
    }

    public void saveAll(List<OrderBook> orderBooks) {
        orderBookRepository.saveAll(orderBooks);
    }
}
