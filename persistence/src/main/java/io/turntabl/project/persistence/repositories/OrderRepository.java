package io.turntabl.project.persistence.repositories;

import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByPortfolio(Portfolio portfolio);

    @Modifying
    @Query("update #{#entityName} set portfolio = :to where portfolio = :from")
    void changePortfolioForOrders(@Param("from") Portfolio from,
                                  @Param("to") Portfolio to);

}
