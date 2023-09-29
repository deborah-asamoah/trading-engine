package io.turntabl.project.marketdataservice.service;

import io.turntabl.project.exchangeclient.ExchangeClient;
import io.turntabl.project.exchangeclient.enums.Exchange;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExchangeClientServiceTest {

    private ExchangeClientService underTest = new ExchangeClientService();

    @Test
    public void setsUpExchangeClientsAfterBeanInitialised() {
        underTest.setApiKey("eced7e6b-9223-414b-ae24-6a911c1aeadb");
        underTest.setUpExchangeClients();
        Map<Exchange, ExchangeClient> clientMap = underTest.getExchangeClientMap();

        assertEquals(Exchange.values().length, clientMap.size());
        assertNotNull(clientMap.get(Exchange.MAL2));
        assertNotNull(clientMap.get(Exchange.MAL1));
    }
}