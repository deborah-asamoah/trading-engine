package io.turntabl.project.clientservice.controllers;

import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientservice.mappers.ClientRequestBodyMapper;
import io.turntabl.project.clientservice.services.ClientDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("clients")
@RestController
public class ClientController {

    private final ClientDataService clientService;
    private final ClientRequestBodyMapper clientMapper;

    public ClientController(ClientDataService clientService,
                            ClientRequestBodyMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping("{id}/account-balance")
    @ResponseStatus(HttpStatus.OK)
    double getAccountBalance(@PathVariable("id") UUID id) {
        return clientService.getAccountBalance(id);
    }

    @GetMapping("{id}/stock-balance")
    @ResponseStatus(HttpStatus.OK)
    double getStockBalance(@PathVariable("id") UUID id) {
        return clientService.getStockBalance(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ClientDTO> getClients() {
        return clientService.getClients()
                .stream()
                .map(clientMapper::toClientDTO)
                .toList();
    }
}
