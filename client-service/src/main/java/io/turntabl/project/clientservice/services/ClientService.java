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
import io.turntabl.project.clientservice.exceptions.InvalidPasswordException;
import io.turntabl.project.clientservice.exceptions.NameCannotBeBlank;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected void validateClientName(String name) throws NameCannotBeBlank{
        if(name == null)
            throw new NameCannotBeBlank();
    }

    protected void validateClientEmail(String email) {
        if(email == null) {
            throw new BadCredentialsException("Email Cannot be null");
        }
        if (!isValidEmail(email)) {
            throw new BadCredentialsException("Email is invalid or null");
        }
        if (clientRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExists();
        }


    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    protected void validateClientPassword (String password) throws InvalidPasswordException {
        if (password == null){
            throw new InvalidPasswordException("Password is invalid");
        }
        if (password.length() < 8){
            throw new InvalidPasswordException("Password must contain at least 8 characters");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidPasswordException("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new InvalidPasswordException("Password must contain at least one special character: !@#$%^&*().");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new InvalidPasswordException("Password must contain at least one digit");
        }
    }

    protected Client saveClientDetailsToClientRepository (RegisterClientRequestBody registerClientRequestBody){
        Client client = new Client(
                registerClientRequestBody.getName().trim(),
                registerClientRequestBody.getEmail().trim(),
                encoder.encode(registerClientRequestBody.getPassword().trim()
                )
        );
        client.setRole(Role.USER);

        Client createdClient = clientRepository.save(client);
        return createdClient;
    }

    protected String generateJwtToken(Client createdClient) {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(
                createdClient.getId(),
                createdClient.getName(),
                createdClient.getEmail(),
                createdClient.getPassword(),
                createdClient.getRole()
        );

        String jwt = jwtUtils.generateJwtToken(userDetailsImpl);
        return jwt;
    }

    protected void createDefaultPortfolio (Client createdClient) {
        CreatePortfolioRequestBody createPortfolioRequestBody = new CreatePortfolioRequestBody();
        createPortfolioRequestBody.setClientID(createdClient.getId());
        createPortfolioRequestBody.setName("Default Portfolio");

        Portfolio newPortfolio = portfolioRequestBodyMapper.toPortfolio(createPortfolioRequestBody);
        newPortfolio.setClient(createdClient);
        newPortfolio.setDefaultPortfolio(true);

        Portfolio portfolio = portfolioRepository.save(newPortfolio);
    }



    public RegisterClientResponseBody registerClient (RegisterClientRequestBody registerClientRequestBody) throws NameCannotBeBlank, InvalidPasswordException {
        validateClientName(registerClientRequestBody.getName());
        validateClientEmail(registerClientRequestBody.getEmail());
        validateClientPassword(registerClientRequestBody.getPassword());

        Client createdClient = saveClientDetailsToClientRepository(registerClientRequestBody);
        String jwt = generateJwtToken(createdClient);
        createDefaultPortfolio(createdClient);

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
