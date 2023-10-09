package io.turntabl.project.clientservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.AuthenticateClientRequestBody;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.RegisterClientRequestBody;
import io.turntabl.project.clientservice.services.ClientService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ClientAuthControllerTest {

    private static final String BASEURL = "/api/auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ClientService clientService;

    RegisterClientRequestBody register;
    AuthenticateClientRequestBody login;

    @BeforeEach
    void setup() {
        final String email = "jake@gmail.com";
        final String password = "123456";
         register = new RegisterClientRequestBody();
        register.setName("Emmanuel");
        register.setEmail(email);
        register.setPassword(password);

        login = new AuthenticateClientRequestBody();
        login.setEmail(email);
        login.setPassword(password);

    }

    @Test
    @DisplayName("When calling the register endpoint should return status of created")
    public void testRegistration() throws Exception {
        String registerBody = objectMapper.writeValueAsString(register);
        RequestBuilder builder = MockMvcRequestBuilders.post(BASEURL+"/register")
                .content(registerBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isCreated();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When calling the login endpoint should return status of created")
    public void testLogin() throws Exception {
        String loginBody = objectMapper.writeValueAsString(register);
        RequestBuilder builder = MockMvcRequestBuilders.post(BASEURL+"/login")
                .content(loginBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isCreated();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When calling the endpoint end to get client details should return status of ok")
    public void testGetClientDetails() throws Exception {

        RequestBuilder builder = MockMvcRequestBuilders.get(BASEURL+"/client/"+ UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedStatus = MockMvcResultMatchers.status()
                .isOk();
        mockMvc.perform(builder).andExpect(expectedStatus).andDo(MockMvcResultHandlers.print());
    }
}