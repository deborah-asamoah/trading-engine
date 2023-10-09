package io.turntabl.project.clientservice.services;

import io.turntabl.project.clientprocessingapi.dtos.ClientDTO;
import io.turntabl.project.clientprocessingapi.dtos.PortfolioDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.clientprocessingapi.enums.Role;
import io.turntabl.project.clientservice.mappers.ClientRequestBodyMapper;
import io.turntabl.project.clientservice.mappers.OrderViewDtoMapper;
import io.turntabl.project.clientservice.mappers.PortfolioRequestBodyMapper;
import io.turntabl.project.orderprocessingapi.dtos.OrderViewDto;
import io.turntabl.project.orderprocessingapi.dtos.responsebodies.ListOrdersResponseBody;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.entities.OrderView;
import io.turntabl.project.persistence.entities.Portfolio;
import io.turntabl.project.persistence.repositories.OrderRepository;
import io.turntabl.project.persistence.repositories.OrderViewRepository;
import io.turntabl.project.persistence.repositories.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @InjectMocks
    PortfolioService underTest;

    @Mock
    PortfolioRepository portfolioRepository;

    @Mock
    PortfolioRequestBodyMapper portfolioRequestBodyMapper;

    @Mock
    ClientService clientService;

    @Mock
    ClientRequestBodyMapper clientRequestBodyMapper;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderViewRepository orderViewRepository;

    @Mock
    OrderViewDtoMapper orderViewDtoMapper;

    @Mock
    OrderView orderView;

    UUID uuid, secondUUID;
    String token;
    CreatePortfolioRequestBody createPortfolioRequestBody;
    ClientDTO clientDTO;
    Client client;
    Portfolio portfolio, secondPortfolio, defaultPortfolio;
    PortfolioDTO portfolioDTO, secondPortfolioDTO;
    OrderViewDto orderViewDto;
    ListOrdersResponseBody listOrdersResponseBody;


    @BeforeEach
    void setUp (){
        uuid = UUID.fromString("c5b007d0-95b6-4f35-b010-339ab5669d20");
        secondUUID = UUID.fromString("a6cdc135-ed37-49cc-8da2-5dcd13e133a4");
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        createPortfolioRequestBody = new CreatePortfolioRequestBody();
        createPortfolioRequestBody.setName("My portfolio");
        createPortfolioRequestBody.setClientID(uuid);

        clientDTO = new ClientDTO();
        clientDTO.setId(uuid);
        clientDTO.setName("John Doe");
        clientDTO.setEmail("johndoe@example.com");
        clientDTO.setPassword("password");
        clientDTO.setRole(String.valueOf(Role.USER));
        clientDTO.setToken(token);

        client = new Client("John Doe", "johndoe@example.com", "password");
        client.setRole(Role.USER);
        client.setId(uuid);

        portfolio = new Portfolio();
        portfolio.setId(uuid);
        portfolio.setClient(client);
        portfolio.setName("My portfolio");
        portfolio.setDefaultPortfolio(false);

        secondPortfolio = new Portfolio();
        secondPortfolio.setId(secondUUID);
        secondPortfolio.setClient(client);
        secondPortfolio.setName("My second portfolio");
        secondPortfolio.setDefaultPortfolio(false);


        defaultPortfolio = new Portfolio();
        defaultPortfolio.setId(uuid);
        defaultPortfolio.setClient(client);
        defaultPortfolio.setName("Default Portfolio");
        defaultPortfolio.setDefaultPortfolio(true);

        portfolioDTO = new PortfolioDTO();
        portfolioDTO.setId(uuid);
        portfolioDTO.setName("My portfolio");
        portfolioDTO.setDefaultPortfolio(false);
        portfolioDTO.setClientID(uuid);

        secondPortfolioDTO = new PortfolioDTO();
        secondPortfolioDTO.setId(secondUUID);
        secondPortfolioDTO.setName("My second portfolio");
        secondPortfolioDTO.setDefaultPortfolio(false);
        secondPortfolioDTO.setClientID(uuid);

        orderView = new OrderView();
        orderViewDto = new OrderViewDto();
        listOrdersResponseBody = new ListOrdersResponseBody();



    }

    @Test
    void createPortfolioIfClientExists (){
        when(clientService.getClient(any(UUID.class))).thenReturn(clientDTO);
        when(clientRequestBodyMapper.toClient(any(ClientDTO.class))).thenReturn(client);
        when(portfolioRequestBodyMapper.toPortfolio(any(CreatePortfolioRequestBody.class))).thenReturn(portfolio);
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);
        when(portfolioRequestBodyMapper.toPortfolioDTO(any(Portfolio.class))).thenReturn(portfolioDTO);

        PortfolioDTO responseBody = underTest.createPortfolio(createPortfolioRequestBody);

        assertNotNull(responseBody);
        assertEquals(portfolioDTO, responseBody);
        verify(clientService, times(1)).getClient(any(UUID.class));
        verify(clientRequestBodyMapper, times(1)).toClient(any(ClientDTO.class));
        verify(portfolioRequestBodyMapper, times(1)).toPortfolio(any(CreatePortfolioRequestBody.class));
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
        verify(portfolioRequestBodyMapper, times(1)).toPortfolioDTO(any(Portfolio.class));
    }


    @Test
    void getClientPortfoliosIfClientExists (){
        when(portfolioRepository.findByClientId(uuid)).thenReturn(Stream.of(portfolio, secondPortfolio).collect(Collectors.toList()));
        when(portfolioRequestBodyMapper.toPortfolioDTO(any(Portfolio.class))).thenReturn(portfolioDTO);

        List<PortfolioDTO> responseBody = underTest.getClientPortfolios(uuid);

        assertEquals(2, responseBody.size());
        assertNotNull(responseBody);
        verify(portfolioRepository, times(1)).findByClientId(any(UUID.class));
        verify(portfolioRequestBodyMapper, times(2)).toPortfolioDTO(any(Portfolio.class));
    }

    @Test
    void getPortfoliosOrdersIfPortfolioExists (){

        when(orderViewRepository.findByPortfolio(any(UUID.class))).thenReturn(Stream.of(orderView).collect(Collectors.toList()));
        when(orderViewDtoMapper.toOrderViewDto(any(OrderView.class))).thenReturn(orderViewDto);

        ListOrdersResponseBody responseBody = underTest.getPortfolioOrders(uuid);

        assertNotNull(responseBody);
        verify(orderViewRepository, times(1)).findByPortfolio(any(UUID.class));
        verify(orderViewDtoMapper, times(1)).toOrderViewDto(any(OrderView.class));
    }


    @Test
    void findDefaultPortfolioForCLientWhenClientExists (){
        when(portfolioRepository.getByClientAndDefaultPortfolio(any(Client.class),any(Boolean.class))).thenReturn(defaultPortfolio);
        Portfolio responseBody = underTest.findDefaultPortfolioForClient(client);

        assertNotNull(responseBody);
        verify(portfolioRepository, times(1)).getByClientAndDefaultPortfolio(any(Client.class),any(Boolean.class));
    }


    @Test
    void deletePortfoliosIfPortfolioExists (){

        underTest.deletePortfolio(portfolio);

        verify(orderRepository, times(1)).changePortfolioForOrders(any(), any());
        verify(portfolioRepository, times(1)).delete(any(Portfolio.class));


    }


















}