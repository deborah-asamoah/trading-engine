package io.turntabl.project.reportingcontract.repositories;

import io.turntabl.project.reportingcontract.entities.ClientEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface ClientEventRepository extends JpaRepository<ClientEvent, UUID> {
    List<ClientEvent> findByOrderByHappenedOnDesc();
}
