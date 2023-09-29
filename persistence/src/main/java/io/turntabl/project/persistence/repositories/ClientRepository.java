package io.turntabl.project.persistence.repositories;

import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.OrderLeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);

}
