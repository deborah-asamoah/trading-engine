package io.turntabl.project.clientservice.controllers;

import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.AuthenticateClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.RegisterClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.responsebodies.AuthenticateClientResponseBody;
import io.turntabl.project.clientprocessingapi.dtos.responsebodies.RegisterClientResponseBody;
import io.turntabl.project.clientservice.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class ClientAuthController {
    private final ClientService clientService;

    public ClientAuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterClientResponseBody registerUser(@RequestBody RegisterClientRequestBody registerClientRequestBody) {
        return clientService.registerClient(registerClientRequestBody);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticateClientResponseBody authenticateUser(@RequestBody AuthenticateClientRequestBody authenticateClientRequestBody) {
        return clientService.authenticateClient(authenticateClientRequestBody);
    }

    @GetMapping("/client/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO getClient(@PathVariable UUID id) {
        return clientService.getClient(id);
    }
}
