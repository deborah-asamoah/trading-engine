package io.turntabl.project.reportingservice.controllers;

import io.turntabl.project.reportingcontract.entities.ClientEvent;
import io.turntabl.project.reportingservice.services.ClientEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("client-events")
public class ClientEventController {

    private final ClientEventService clientEventService;

    public ClientEventController(ClientEventService clientEventService) {
        this.clientEventService = clientEventService;
    }

    @GetMapping
    public List<ClientEvent> readClientEvents() {
        return clientEventService.findAll();
    }
}
