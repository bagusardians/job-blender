package com.job.blender;

import com.google.gson.Gson;
import com.job.blender.service.*;
import okhttp3.OkHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        InputStream input = App.class.getClassLoader().getResourceAsStream("config.json");
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        Config config = gson.fromJson(reader, Config.class);

        OkHttpClient okHttpClient = new OkHttpClient();
        AddressService addressService = new AddressServiceImpl();
        MixService mixService = new MixServiceImpl(okHttpClient, addressService, config);
        FeeService feeService = new FeeServiceImpl(config);
        DoleService doleService = new DoleServiceImpl(config, okHttpClient, feeService);
        PollService pollService = new PollServiceImpl(
                okHttpClient,
                addressService,
                mixService,
                doleService,
                config
        );

        CoinPoller poller = new CoinPoller(pollService);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(poller, 0, 5, TimeUnit.SECONDS);

        Scanner s = new Scanner(System.in);
        System.out.println("Welcome to jobcoin blender");
        while(true) {
            System.out.println("what do you want to do? (choose the number)");
            System.out.println("1 Create deposit address.");
            System.out.println("2 exit.");
            String inp = s.next();
            if (inp.equals("1")) {
                System.out.println("put in your addresses, separated by comma.");
                inp = s.next();
                inp = inp.replaceAll("\\s+","");
                List<String> addresses = Arrays.asList(inp.split(","));
                String depositAddress = addressService.createDepositAddress(addresses);
                System.out.println("Please deposit your coin to this address: " + depositAddress);
            } else if(inp.equals("2")) {
                System.exit(0);
            } else {
                System.out.println("invalid input.");
            }

        }
    }
}
