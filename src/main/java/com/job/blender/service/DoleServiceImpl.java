package com.job.blender.service;

import com.google.gson.Gson;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DoleServiceImpl implements DoleService {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Gson GSON = new Gson();

    private final Config config;
    private final OkHttpClient okHttpClient;
    private final Queue<Transaction> queue;
    private final FeeService feeService;

    public DoleServiceImpl(Config config, OkHttpClient okHttpClient, FeeService feeService) {
        this.config = config;
        this.okHttpClient = okHttpClient;
        this.feeService = feeService;
        this.queue = new ArrayDeque<>();

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = this::pollAndSend;
        ses.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void dole(BigDecimal amount, List<String> destinationAddresses) {
        BigDecimal finalAmount = feeService.collectFee(amount);
        BigDecimal roughAmount = finalAmount
                .divide(BigDecimal.valueOf(destinationAddresses.size()), 2, RoundingMode.DOWN);

        System.out.println("dole transaction " + destinationAddresses.size());

        for (int i = 0; i < destinationAddresses.size(); i++) {
            String address = destinationAddresses.get(i);
            System.out.println("doling out to " + address);
            if (i < destinationAddresses.size() - 1) {
                queue.add(new Transaction(config.houseAddress, address, roughAmount));
                finalAmount = finalAmount.subtract(roughAmount);
            } else {
                queue.add(new Transaction(config.houseAddress, address, finalAmount));
            }
        }
    }

    Queue<Transaction> getQueue() {
        return queue;
    }

    private void pollAndSend() {
        if (queue.isEmpty()) {
            return;
        }
        send(queue.poll());
    }

    private void send(Transaction transaction) {
        System.out.println("send out the dole " + transaction.toAddress + " " + transaction.amount);
        String json = GSON.toJson(transaction);
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
