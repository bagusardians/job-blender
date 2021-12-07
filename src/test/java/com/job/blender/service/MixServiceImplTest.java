package com.job.blender.service;

import junit.framework.TestCase;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MixServiceImplTest extends TestCase {

    AddressService addressService = new AddressService() {
        @Override
        public String createDepositAddress(List<String> userAddresses) {
            return null;
        }

        @Override
        public List<String> getUserAddresses(String depositAddress) {
            return null;
        }

        @Override
        public boolean isDepositAddressExist(String depositAddress) {
            return true;
        }
    };

    public void testMix_WithDepositTransaction_Success() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start();
        HttpUrl baseUrl = server.url("/v1/api");
        Config config = new Config(baseUrl.toString(), "", "/any", BigDecimal.valueOf(10));
        MixService underTest = new MixServiceImpl(new OkHttpClient(), addressService, config);
        List<Transaction> depositTransactions = Collections.singletonList(new Transaction("from", "to", BigDecimal.TEN));
        boolean result = underTest.mix(depositTransactions, "home");
        assertTrue(result);
        server.shutdown();
    }

    public void testMix_WithEmptyDepositTransaction_Failed() {
        Config config = new Config("", "", "/any", BigDecimal.valueOf(10));
        MixService underTest = new MixServiceImpl(new OkHttpClient(), addressService, config);
        boolean result = underTest.mix(Collections.emptyList(), "home");
        assertFalse(result);
    }
}