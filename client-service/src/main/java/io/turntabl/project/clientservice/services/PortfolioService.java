package io.turntabl.project.clientservice.services;


import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientprocessingapi.dtos.PortfolioDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.clientservice.mappers.ClientRequestBodyMapper;
import io.turntabl.project.clientservice.mappers.OrderViewDtoMapper;
import io.turntabl.project.clientservice.mappers.PortfolioRequestBodyMapper;
import io.turntabl.project.orderprocessingapi.dtos.responsebodies.ListOrdersResponseBody;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.OrderRepository;
import io.turntabl.project.persistence.repositories.OrderViewRepository;
import io.turntabl.project.persistence.repositories.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    private final PortfolioRequestBodyMapper portfolioRequestBodyMapper;

    private final ClientService clientService;

    private final ClientRequestBodyMapper clientRequestBodyMapper;
    private final OrderRepository orderRepository;
    private final OrderViewRepository orderViewRepository;
    private final OrderViewDtoMapper orderViewDtoMapper;

    public PortfolioService(PortfolioRepository portfolioRepository,
                            PortfolioRequestBodyMapper portfolioRequestBodyMapper,
                            ClientService clientService,
                            ClientRequestBodyMapper clientRequestBodyMapper,
                            OrderRepository orderRepository,
                            OrderViewRepository orderViewRepository,
                            OrderViewDtoMapper orderViewDtoMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioRequestBodyMapper = portfolioRequestBodyMapper;
        this.clientService = clientService;
        this.clientRequestBodyMapper = clientRequestBodyMapper;
        this.orderRepository = orderRepository;
        this.orderViewRepository = orderViewRepository;
        this.orderViewDtoMapper = orderViewDtoMapper;
    }

    public Optional<Portfolio> getPortfolioById(UUID id) {
        return portfolioRepository.findById(id);
    }

    public PortfolioDTO createPortfolio(CreatePortfolioRequestBody createPortfolioRequestBody) {
        ClientDTO clientDTO = clientService.getClient(createPortfolioRequestBody.getClientID());
        Client client = clientRequestBodyMapper.toClient(clientDTO);

        Portfolio portfolio = portfolioRequestBodyMapper.toPortfolio(createPortfolioRequestBody);
        portfolio.setDefaultPortfolio(false);
        portfolio.setClient(client);

        Portfolio createdPortfolio = portfolioRepository.save(portfolio);
        return portfolioRequestBodyMapper.toPortfolioDTO(createdPortfolio);
    }


    public List<PortfolioDTO> getClientPortfolios(UUID clientId) {
        return portfolioRepository
                .findByClientId(clientId)
                .stream()
                .map((portfolio) -> {
                    PortfolioDTO portfolioDTO = portfolioRequestBodyMapper.toPortfolioDTO(portfolio);
                    portfolioDTO.setClientID(portfolio.getClient().getId());
                    return portfolioDTO;
                })
                .toList();
    }

    public ListOrdersResponseBody getPortfolioOrders(UUID portfolio) {
        return ListOrdersResponseBody
                .builder()
                .data(orderViewRepository
                        .findByPortfolio(portfolio)
                        .stream()
                        .map(orderViewDtoMapper::toOrderViewDto)
                        .toList())
                .build();
    }

    public Portfolio findById(UUID id) {
        return portfolioRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Portfolio With ID %s Not Found", id)));
    }

    public Portfolio findDefaultPortfolioForClient(Client client) {
        return portfolioRepository
                .getByClientAndDefaultPortfolio(client, true);
    }

    public void deletePortfolio(Portfolio portfolio) {
        if (portfolio.isDefaultPortfolio())
            throw new IllegalArgumentException("Cannot Delete Default Portfolio");

        Portfolio defaultPortfolio = findDefaultPortfolioForClient(portfolio.getClient());

        orderRepository.changePortfolioForOrders(portfolio, defaultPortfolio);

        portfolioRepository.delete(portfolio);
    }

}
