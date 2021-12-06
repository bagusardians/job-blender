package com.job.blender.service;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class PollServiceImpl implements PollService {

    private final OkHttpClient client;
    private final AddressService addressService;
    private final MixService mixService;
    private final DoleService doleService;
    private final Config config;

    public PollServiceImpl(
            OkHttpClient client,
            AddressService addressService,
            MixService mixService,
            DoleService doleService,
            Config config) {
        this.client = client;
        this.addressService = addressService;
        this.mixService = mixService;
        this.doleService = doleService;
        this.config = config;
    }

    @Override
    public void poll() throws JsonParserException, IOException {

        Request request = new Request.Builder()
                .url(config.jobcoinUrl + config.transactionPath)
                .build();

        JsonArray allTransaction;
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            allTransaction = JsonParser.array().from(body);
        }


        // iterate the transactions
        // if exist on the depositAddresses
        List<String> depositAddresses = addressService.getAllDepositAddresses();

        // mix it to house address
        // mixService.mix(unprocessedTransactions, houseAddress);

        // dole it to the actual list of addresses
        // addressService.getUserAddresses(depositAddress);
        // doleService.dole(houseAddress, amount, userAddresses);
    }

}
