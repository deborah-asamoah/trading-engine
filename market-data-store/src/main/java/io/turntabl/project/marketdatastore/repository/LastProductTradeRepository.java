package io.turntabl.project.marketdatastore.repository;

import io.turntabl.project.marketdatastore.entity.LastProductTrade;
import org.springframework.data.repository.CrudRepository;

public interface LastProductTradeRepository extends CrudRepository<LastProductTrade, String> {
}
