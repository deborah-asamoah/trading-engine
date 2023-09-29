package io.turntabl.project.persistence.repositories;

import io.turntabl.project.persistence.entities.OrderView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderViewRepository extends JpaRepository<OrderView, UUID> {
    List<OrderView> findByClient(UUID client);
    List<OrderView> findByPortfolio(UUID portfolio);

    @Query("""
            select o
            from OrderView o
            where o.complete = false and o.cancelled = false
            """)
    List<OrderView> findOpenOrders();

    @Query("""
            select o
            from OrderView o
            where o.complete = true
            """)
    List<OrderView> findFilledOrders();

    @Query("""
            select o
            from OrderView o
            where o.cancelled = true
            """)
    List<OrderView> findCancelledOrders();

    @Query("""
            select
                sum((case
                    when side = io.turntabl.project.orderprocessingapi.enums.OrderSide.BUY then -1
                    when side = io.turntabl.project.orderprocessingapi.enums.OrderSide.SELL then 1
                    else 0
                end) * value)
            from #{#entityName}
            where client = :client
            """)
    double getAccountBalance(@Param("client") UUID client);

    @Query("""
            select
                sum((case
                    when side = io.turntabl.project.orderprocessingapi.enums.OrderSide.BUY then 1
                    when side = io.turntabl.project.orderprocessingapi.enums.OrderSide.SELL then -1
                    else 0
                end) * value)
            from #{#entityName}
            where client = :client
            """)
    double getStockBalance(@Param("client") UUID client);
}
