package io.turntabl.project.reportingservice.services;

import io.turntabl.project.reportingcontract.entities.ClientEvent;
import io.turntabl.project.reportingcontract.repositories.ClientEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientEventService {

    private final ClientEventRepository clientEventRepository;

    public ClientEventService(ClientEventRepository clientEventRepository) {
        this.clientEventRepository = clientEventRepository;
    }

    public List<ClientEvent> findAll() {
        return clientEventRepository
                .findByOrderByHappenedOnDesc();
    }

    public void save(ClientEvent clientEvent) {
        clientEventRepository.save(clientEvent);
    }
}
