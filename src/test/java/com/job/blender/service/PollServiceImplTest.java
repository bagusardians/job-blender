package com.job.blender.service;

import com.google.gson.Gson;
import junit.framework.TestCase;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class PollServiceImplTest extends TestCase {
    AddressService addressService = new AddressService() {
        @Override
        public String createDepositAddress(List<String> userAddresses) {
            return null;
        }

        @Override
        public List<String> getUserAddresses(String depositAddress) {
            return Arrays.asList("user1", "user2");
        }

        @Override
        public boolean isDepositAddressExist(String depositAddress) {
            return true;
        }
    };

    MixService mixService = (depositTransactions, houseAddress) -> false;

    DoleService doleService = (amount, destinationAddresses) -> {

    };

    public void testPoll() throws IOException {
        MockWebServer server = new MockWebServer();
        Transaction first = new Transaction(
                ZonedDateTime.now().plusMinutes(10).withZoneSameInstant(ZoneOffset.UTC).toString(),
                "user1",
                "deposit1",
                BigDecimal.TEN
        );
        Transaction second = new Transaction(
                ZonedDateTime.now().plusMinutes(11).withZoneSameInstant(ZoneOffset.UTC).toString(),
                "user2",
                "deposit2",
                BigDecimal.TEN
        );
        List<Transaction> transactions = Arrays.asList(first, second);
        Gson gson = new Gson();
        server.enqueue(new MockResponse().setBody(gson.toJson(transactions)));
        server.start();
        HttpUrl baseUrl = server.url("/v1/api");
        Config config = new Config(baseUrl.toString(), "house", "/any", BigDecimal.valueOf(10));

        PollServiceImpl underTest = new PollServiceImpl(
                new OkHttpClient(),
                addressService,
                mixService,
                doleService,
                config
        );

        underTest.poll();
    }
}