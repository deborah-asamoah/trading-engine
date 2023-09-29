package io.turntabl.project.clientservice.services;


import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.AuthenticateClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.RegisterClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.responsebodies.AuthenticateClientResponseBody;
import io.turntabl.project.clientprocessingapi.dtos.responsebodies.RegisterClientResponseBody;
import io.turntabl.project.clientprocessingapi.enums.Role;
import io.turntabl.project.clientservice.config.JwtUtils;
import io.turntabl.project.clientservice.exceptions.EmailAlreadyExists;
import io.turntabl.project.clientservice.exceptions.EmailDoesNotExists;
import io.turntabl.project.clientservice.mappers.ClientRequestBodyMapper;
import io.turntabl.project.clientservice.mappers.PortfolioRequestBodyMapper;
import io.turntabl.project.clientservice.userdetails.UserDetailsImpl;
import io.turntabl.project.clientservice.userdetails.UserDetailsServiceImpl;
import io.turntabl.project.clientservice.utils.ClientLoggingUtils;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.ClientRepository;
import io.turntabl.project.persistence.repositories.PortfolioRepository;
import io.turntabl.project.reportingcontract.enums.ClientEventType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    private final PortfolioRequestBodyMapper portfolioRequestBodyMapper;

    private final PortfolioRepository portfolioRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final ClientRequestBodyMapper clientRequestBodyMapper;

    private final UserDetailsServiceImpl userDetailsService;

    private final ClientLoggingUtils loggingUtils;

    public ClientService(ClientRepository clientRepository,
                         PortfolioRequestBodyMapper portfolioRequestBodyMapper,
                         PortfolioRepository portfolioRepository,
                         PasswordEncoder encoder,
                         AuthenticationManager authenticationManager,
                         JwtUtils jwtUtils,
                         ClientRequestBodyMapper clientRequestBodyMapper,
                         UserDetailsServiceImpl userDetailsServiceImpl, ClientLoggingUtils loggingUtils) {
        this.clientRepository = clientRepository;
        this.portfolioRequestBodyMapper = portfolioRequestBodyMapper;
        this.portfolioRepository = portfolioRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.clientRequestBodyMapper = clientRequestBodyMapper;
        this.userDetailsService = userDetailsServiceImpl;
        this.loggingUtils = loggingUtils;
    }

    public RegisterClientResponseBody registerClient (RegisterClientRequestBody registerClientRequestBody) {
        if (clientRepository.findByEmail(registerClientRequestBody.getEmail()).isPresent()) {
            throw new EmailAlreadyExists();
        }

        Client client = new Client(
                registerClientRequestBody.getName(),
                registerClientRequestBody.getEmail(),
                encoder.encode(registerClientRequestBody.getPassword()
                )
        );

        client.setRole(Role.USER);

        Client createdClient = clientRepository.save(client);

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(
                createdClient.getId(),
                createdClient.getName(),
                createdClient.getEmail(),
                createdClient.getPassword(),
                createdClient.getRole()
        );

        String jwt = jwtUtils.generateJwtToken(userDetailsImpl);

        //creating a default portfolio
        CreatePortfolioRequestBody createPortfolioRequestBody = new CreatePortfolioRequestBody();
        createPortfolioRequestBody.setClientID(client.getId());
        createPortfolioRequestBody.setName("Default Portfolio");

        Portfolio newPortfolio = portfolioRequestBodyMapper.toPortfolio(createPortfolioRequestBody);
        newPortfolio.setClient(client);
        newPortfolio.setDefaultPortfolio(true);

        portfolioRepository.save(newPortfolio);

        RegisterClientResponseBody registerClientResponseBody = new RegisterClientResponseBody();
        registerClientResponseBody.setId(createdClient.getId());
        registerClientResponseBody.setEmail(createdClient.getEmail());
        registerClientResponseBody.setName(createdClient.getName());
        registerClientResponseBody.setAccessToken(jwt);
        registerClientResponseBody.setRole(createdClient.getRole());

        loggingUtils.logEvent(ClientEventType.CREATE_CLIENT,createdClient.getId(),
                "CREATED CLIENT: " + createdClient.getId());
        return registerClientResponseBody;
    }


    public AuthenticateClientResponseBody authenticateClient (AuthenticateClientRequestBody authenticateClientRequestBody) {

        if (!clientRepository.findByEmail(authenticateClientRequestBody.getEmail()).isPresent()) {
            throw new EmailDoesNotExists();
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateClientRequestBody.getEmail(),
                        authenticateClientRequestBody.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication);

        AuthenticateClientResponseBody authenticateClientResponseBody = new AuthenticateClientResponseBody();
        authenticateClientResponseBody.setEmail(userDetails.getUsername());
        authenticateClientResponseBody.setName(userDetails.getName());
        authenticateClientResponseBody.setId(userDetails.getId());
        authenticateClientResponseBody.setAccessToken(jwt);
        authenticateClientResponseBody.setRole(userDetails.getRole());
        return authenticateClientResponseBody;
    }




    public ClientDTO getClient(UUID id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));

        return clientRequestBodyMapper.toClientDTO(client);
    }
}
