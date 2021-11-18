package com.job.blender.service;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.InputStream;
import java.util.List;

public class PollServiceImpl implements PollService {

    private final OkHttpClient client;
    private final AddressService addressService;
    private final MixService mixService;
    private final DoleService doleService;

    PollServiceImpl(
            OkHttpClient client,
            AddressService addressService,
            MixService mixService,
            DoleService doleService
    ) {
        this.client = client;
        this.addressService = addressService;
        this.mixService = mixService;
        this.doleService = doleService;
    }

    @Override
    public void poll() {
        // get config
//        InputStream input = getClass().getClassLoader().getResourceAsStream("config.json");
//        JsonObject object = JsonParser.object().from(input);
//        String url = object.getString("jobcoinUrl");
//        String transactionPath = object.getString("transactionPath");
//        String houseAddress = object.getString("houseAddress");
//
//        Request request = new Request.Builder()
//                .url(url + transactionPath)
//                .build();
//
//        JsonObject allTransaction;
//        try (Response response = client.newCall(request).execute()) {
//            allTransaction = JsonParser.object().from(response.body().string());
        }


        // iterate the transactions
        // if exist on the depositAddresses
        //List<String> depositAddresses = addressService.getAllDepositAddresses();

        // mix it to house address
        // mixService.mix(unprocessedTransactions, houseAddress);

        // dole it to the actual list of addresses
        // addressService.getUserAddresses(depositAddress);
        // doleService.dole(houseAddress, amount, userAddresses);
}
