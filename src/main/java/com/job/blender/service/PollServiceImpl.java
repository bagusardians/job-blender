package com.job.blender.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class PollServiceImpl implements PollService {

    private final OkHttpClient client;
    private final AddressService addressService;
    private final MixService mixService;
    private final DoleService doleService;
    private final Config config;
    private ZonedDateTime latestTransactionTimestamp;

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
        this.latestTransactionTimestamp = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
    }

    @Override
    public void poll() throws IOException {
        Request request = new Request.Builder()
                .url(config.jobcoinUrl + config.transactionPath)
                .build();

        List<Transaction> allTransaction;
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Transaction>>(){}.getType();
            allTransaction = gson.fromJson(body, listType);
        }

        List<Transaction> unprocessedTransactions = new ArrayList<>();
        for (int i = allTransaction.size() - 1; i >= 0; i--) {
            Transaction transaction = allTransaction.get(i);
            ZonedDateTime transDateTime = ZonedDateTime.parse(transaction.timestamp);
            if (transDateTime.isEqual(latestTransactionTimestamp) || transDateTime.isBefore(latestTransactionTimestamp)) {
                break;
            }
            boolean isValid = addressService.isDepositAddressExist(transaction.toAddress);
            if (isValid) {
                unprocessedTransactions.add(transaction);
            }
        }
        Transaction latestTrans = allTransaction.get(allTransaction.size() - 1);
        ZonedDateTime latestTransTimestamp = ZonedDateTime.parse(latestTrans.timestamp);
        if (latestTransTimestamp.isAfter(latestTransactionTimestamp)) {
            latestTransactionTimestamp = ZonedDateTime.parse(latestTrans.timestamp);
        }
        if (unprocessedTransactions.size() == 0) {
            return;
        }

        mixService.mix(unprocessedTransactions, config.houseAddress);

        for (Transaction transaction : unprocessedTransactions) {
            List<String> userAddresses = addressService.getUserAddresses(transaction.toAddress);
            if (userAddresses == null || userAddresses.size() == 0) {
                continue;
            }
            doleService.dole(transaction.amount, userAddresses);
        }
    }

}
