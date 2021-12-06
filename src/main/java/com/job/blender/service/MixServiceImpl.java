package com.job.blender.service;

import com.google.gson.Gson;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class MixServiceImpl implements MixService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    private final OkHttpClient okHttpClient;
    private final AddressService addressService;
    private final Config config;

    public MixServiceImpl(OkHttpClient okHttpClient, AddressService addressService, Config config) {
        this.okHttpClient = okHttpClient;
        this.addressService = addressService;
        this.config = config;
    }


    @Override
    public void mix(List<Transaction> depositTransactions, String houseAddress) {
        for(Transaction transaction : depositTransactions) {
            if (addressService.isDepositAddressExist(transaction.toAddress)) {
                send(transaction.toAddress, houseAddress, transaction.amount);
            }
        }
    }

    private void send(String fromAccount, String toAccount, BigDecimal amount) {
        String json = GSON.toJson(new Transaction(fromAccount, toAccount, amount));
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(config.jobcoinUrl + config.transactionPath)
                .post(requestBody)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String body = response.body().string();
            JsonParser.object().from(body);
        } catch (IOException | JsonParserException e) {
            e.printStackTrace();
        }
    }
}
