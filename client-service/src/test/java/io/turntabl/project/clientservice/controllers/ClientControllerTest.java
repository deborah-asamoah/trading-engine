package io.turntabl.project.clientservice.controllers;

import io.turntabl.project.clientservice.services.ClientDataService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

class ClientControllerTest {
    final static String BASEURL = "/clients";

    @MockBean
    ClientDataService clientDataService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("When get clients endpoint is hit should return status of ok")
    @WithMockUser
    public void testGetClients() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL)
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isOk();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When get stock balance endpoint is hit should return status of ok")
    @WithMockUser
    public void testGetStockBalance() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL+"/"+ UUID.randomUUID()+"/stock-balance")
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isOk();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When get account balance endpoint is hit should return status of ok")
    @WithMockUser
    public void testGetAccountBalance() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL+"/"+ UUID.randomUUID()+"/account-balance")
                .contentType(MediaType.APPLICATION_JSON).headers(HttpHeaders.EMPTY);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isOk();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

}