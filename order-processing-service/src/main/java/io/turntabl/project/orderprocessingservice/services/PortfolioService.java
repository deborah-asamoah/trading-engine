package io.turntabl.project.orderprocessingservice.services;

import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio findById(UUID id) {
        return portfolioRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Portfolio With ID %s Not Found", id)));
    }

}
