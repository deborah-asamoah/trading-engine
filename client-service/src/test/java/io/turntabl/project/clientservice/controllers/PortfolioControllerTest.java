package io.turntabl.project.clientservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.clientprocessingapi.enums.Role;
import io.turntabl.project.clientservice.services.PortfolioService;
import io.turntabl.project.clientservice.userdetails.UserDetailsImpl;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class PortfolioControllerTest {

    final static String BASEURL = "/api/v1/portfolio";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PortfolioService portfolioService;


    CreatePortfolioRequestBody createPortfolio;

    UserDetailsImpl userDetails;
    @BeforeEach
    void setUp() {
        createPortfolio = new CreatePortfolioRequestBody();
        createPortfolio.setClientID(UUID.randomUUID());
        createPortfolio.setName("MyPortfolio");

        userDetails = new UserDetailsImpl(UUID.randomUUID(),"Emmanuel","jake@gmail.com","123456", Role.USER);
    }

    @Test
    @DisplayName("When create portfolio endpoint is hit should return status of accepted")
    @WithMockUser
    public void testCreatePortfolio() throws Exception {
        String createPortfolioBody = objectMapper.writeValueAsString(createPortfolio);
        RequestBuilder builder = MockMvcRequestBuilders.post(BASEURL)
                .content(createPortfolioBody)
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isAccepted();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When get Client Portfolios endpoint is hit should return status of ok")
    @WithMockUser
    public void testGetClientPortfolios() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL+"/client/"+UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isOk();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When get Portfolio orders endpoint is hit should return status not found ")
    @WithMockUser
    public void testGetPortfolioOrdersShouldReturnNotFound() throws Exception {
        UUID portfolioId = UUID.randomUUID();
        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL+"/"+portfolioId+"/orders")
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedResult = MockMvcResultMatchers.status().isNotFound();
        mockMvc.perform(builder).andExpect(expectedResult);
    }

    @Test
    @DisplayName("When delete portfolio endpoint is hit should return status ok ")
    @WithMockUser
    public void testDeletePortfolioShouldOk() throws Exception {
        UUID portfolioId = UUID.randomUUID();
        RequestBuilder builder = MockMvcRequestBuilders.delete(BASEURL+"/"+portfolioId)
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedResult = MockMvcResultMatchers.status().isOk();
        mockMvc.perform(builder).andExpect(expectedResult);
    }


}