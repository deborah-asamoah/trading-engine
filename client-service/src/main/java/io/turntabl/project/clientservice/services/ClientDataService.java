package io.turntabl.project.clientservice.services;

import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.repositories.ClientRepository;
import io.turntabl.project.persistence.repositories.OrderViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientDataService {
    private final OrderViewRepository orderViewRepository;
    private final ClientRepository clientRepository;

    public ClientDataService(OrderViewRepository orderViewRepository,
                             ClientRepository clientRepository) {
        this.orderViewRepository = orderViewRepository;
        this.clientRepository = clientRepository;
    }

    public double getAccountBalance(UUID id) {
        return orderViewRepository.getAccountBalance(id);
    }

    public double getStockBalance(UUID id) {
        return orderViewRepository.getStockBalance(id);
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }
}
