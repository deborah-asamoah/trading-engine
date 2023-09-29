package io.turntabl.project.persistence.repositories;

import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.UUID;


@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    List<Portfolio> findByClientId(UUID client);

    Portfolio getByClientAndDefaultPortfolio(Client client, boolean defaultPortfolio);



}
