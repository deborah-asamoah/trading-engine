package io.turntabl.project.persistence.repositories;

import io.turntabl.project.persistence.entities.Order;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface OrderLegRepository extends JpaRepository<OrderLeg, UUID> {
    Optional<OrderLeg> findByExchangeOrderId(String exchangeOrderId);

    List<OrderLeg> findByOrder(Order order);
}
