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
import io.turntabl.project.clientservice.utils.ClientLoggingUtils;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.ClientRepository;
import io.turntabl.project.persistence.repositories.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    ClientService underTest;

    @Mock
    ClientRepository clientRepository;

    @Mock
    PortfolioRequestBodyMapper portfolioRequestBodyMapper;

    @Mock
    PortfolioRepository portfolioRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    ClientRequestBodyMapper clientRequestBodyMapper;

    @Mock
    ClientLoggingUtils clientLoggingUtils;

    RegisterClientRequestBody registerClientRequestBody;
    RegisterClientResponseBody registerClientResponseBody;
    CreatePortfolioRequestBody createPortfolioRequestBody;
    Client client, createdClient;
    Portfolio newPortfolio, createdPortfolio;
    AuthenticateClientRequestBody authenticateClientRequestBody;
    AuthenticateClientResponseBody authenticateClientResponseBody;
    UUID uuid;
    ClientDTO clientDTO;

    @BeforeEach
    void setUp (){
        uuid = UUID.fromString("c5b007d0-95b6-4f35-b010-339ab5669d20");
        registerClientRequestBody = new RegisterClientRequestBody();
        registerClientRequestBody.setName("John Doe");
        registerClientRequestBody.setEmail("johndoe@example.com");
        registerClientRequestBody.setPassword("password");

        authenticateClientRequestBody = new AuthenticateClientRequestBody();
        authenticateClientRequestBody.setEmail("johndoe@example.com");
        authenticateClientRequestBody.setPassword("password");
    }

    @Test
    void registerClientWHenEmailDoesNotExists (){
        when(clientRepository.findByEmail(registerClientRequestBody.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerClientRequestBody.getPassword())).thenReturn("encodedPassword");

        client = new Client(registerClientRequestBody.getName(), registerClientRequestBody.getEmail(), "encodedPassword");
        client.setRole(Role.USER);

        createdClient = new Client(client.getName(), client.getEmail(), client.getPassword());
        createdClient.setRole(client.getRole());
        createdClient.setId(UUID.randomUUID());

        when(clientRepository.save(any(Client.class))).thenReturn(createdClient);

        when(jwtUtils.generateJwtToken(any(UserDetailsImpl.class))).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        createPortfolioRequestBody = new CreatePortfolioRequestBody();
        createPortfolioRequestBody.setClientID(client.getId());
        createPortfolioRequestBody.setName("Default Portfolio");

        newPortfolio = new Portfolio();
        newPortfolio.setName(createPortfolioRequestBody.getName());

        when(portfolioRequestBodyMapper.toPortfolio(any(CreatePortfolioRequestBody.class))).thenReturn(newPortfolio);

        newPortfolio.setClient(client);
        newPortfolio.setDefaultPortfolio(true);

        createdPortfolio = new Portfolio();
        createdPortfolio.setClient(client);
        createdPortfolio.setName("Default Portfolio");
        createdPortfolio.setDefaultPortfolio(true);
        createdPortfolio.setId(UUID.randomUUID());

        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(createdPortfolio);

        registerClientResponseBody = new RegisterClientResponseBody(createdClient.getId(), createdClient.getName(), createdClient.getEmail(), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",createdClient.getRole());


        RegisterClientResponseBody responseBody = underTest.registerClient(registerClientRequestBody);

        //then
        assertNotNull(responseBody);
        assertEquals(registerClientResponseBody, responseBody);

        verify(clientRepository, times(1)).save(any(Client.class));
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
        verify(jwtUtils, times(1)).generateJwtToken((any(UserDetailsImpl.class)));
        verify(portfolioRequestBodyMapper, times(1)).toPortfolio(any(CreatePortfolioRequestBody.class));

    }

    @Test
    void registerClientWhenEmailAlreadyExists (){
        when(clientRepository.findByEmail(registerClientRequestBody.getEmail())).thenReturn(Optional.of(new Client("John Doe", "johndoe@example.com", "password")));

        assertThrows(EmailAlreadyExists.class, ()-> underTest.registerClient(registerClientRequestBody));
        verify(clientRepository, times(1)).findByEmail(any());
        verify(clientRepository, times(0)).save(any(Client.class));
        verify(portfolioRepository, times(0)).save(any(Portfolio.class));
        verify(jwtUtils, times(0)).generateJwtToken((any(UserDetailsImpl.class)));
        verify(portfolioRequestBodyMapper, times(0)).toPortfolio(any(CreatePortfolioRequestBody.class));
    }

    @Test
    void authenticateClientWhenEmailExists (){
        when(clientRepository.findByEmail(authenticateClientRequestBody.getEmail())).thenReturn(Optional.of(new Client()));

        UserDetailsImpl userDetails = new UserDetailsImpl(
                uuid,
                "John Doe",
                authenticateClientRequestBody.getEmail(),
                authenticateClientRequestBody.getPassword(),
                Role.USER
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                authenticateClientRequestBody.getPassword()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        authenticateClientResponseBody = new AuthenticateClientResponseBody(
                uuid,
                "John Doe",
                authenticateClientRequestBody.getEmail(),
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                Role.USER);

        AuthenticateClientResponseBody responseBody = underTest.authenticateClient(authenticateClientRequestBody);

        assertNotNull(responseBody);
        assertEquals(authenticateClientResponseBody, responseBody);
        verify(clientRepository, times(1)).findByEmail(any());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(any(Authentication.class));
    }

    @Test
    void authenticateClientWhenEmailDoesNotExists (){
        when(clientRepository.findByEmail(authenticateClientRequestBody.getEmail())).thenReturn(Optional.empty());

        assertThrows(EmailDoesNotExists.class, ()-> underTest.authenticateClient(authenticateClientRequestBody));
        verify(clientRepository, times(1)).findByEmail(any());
        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(0)).generateJwtToken(any(Authentication.class));
    }

    @Test
    void getClientByIdIfClientExists (){
        client = new Client("John Doe", "johndoe@example.com", "password");
        client.setId(uuid);
        client.setRole(Role.USER);

        clientDTO = new ClientDTO();
        clientDTO.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        clientDTO.setRole(String.valueOf(Role.USER));
        clientDTO.setName(client.getName());
        clientDTO.setPassword(client.getPassword());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setId(client.getId());

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(client));

        when(clientRequestBodyMapper.toClientDTO(any(Client.class))).thenReturn(clientDTO);

        ClientDTO responseBody = underTest.getClient(uuid);

        assertEquals(clientDTO, responseBody);
        verify(clientRepository, times(1)).findById(uuid);
        verify(clientRequestBodyMapper, times(1)).toClientDTO(client);
    }


    @Test
    void getClientByIdIfClientDoesNotExists (){
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> underTest.getClient(uuid));
        verify(clientRepository, times(1)).findById(uuid);
        verify(clientRequestBodyMapper, times(0)).toClientDTO(client);
    }

}
