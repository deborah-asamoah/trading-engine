package io.turntabl.project.clientservice.controllers;


import io.turntabl.project.clientprocessingapi.dtos.PortfolioDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.responsebodies.ListPortfoliosResponseBody;
import io.turntabl.project.clientservice.exceptions.PortfolioDoesNotExistException;
import io.turntabl.project.clientservice.services.PortfolioService;
import io.turntabl.project.orderprocessingapi.dtos.responsebodies.ListOrdersResponseBody;
import io.turntabl.project.persistence.entities.Portfolio;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PortfolioDTO createPortfolio(@RequestBody CreatePortfolioRequestBody createPortfolioRequestBody) {
        return portfolioService.createPortfolio(createPortfolioRequestBody);
    }

    @GetMapping("/client/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ListPortfoliosResponseBody getClientPortfolios(@PathVariable("id") UUID clientId) {
        List<PortfolioDTO> portfolios = portfolioService.getClientPortfolios(clientId);
        return new ListPortfoliosResponseBody(portfolios);
    }

    @GetMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.OK)
    public ListOrdersResponseBody getPortfolioOrders(@PathVariable("id") UUID portfolioId) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioById(portfolioId);
        if (portfolio.isEmpty()) {
            throw new PortfolioDoesNotExistException("Portfolio: " + portfolioId + " does not exist");
        }
        return portfolioService.getPortfolioOrders(portfolioId);
    }

    @Transactional
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePortfolio(@PathVariable("id") UUID portfolioId) {
        Portfolio portfolio = portfolioService.findById(portfolioId);
        portfolioService.deletePortfolio(portfolio);
    }
}
