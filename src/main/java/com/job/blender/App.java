package com.job.blender;

import com.google.gson.Gson;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.job.blender.service.*;
import okhttp3.OkHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws JsonParserException, UnsupportedEncodingException {
        // get config
        InputStream input = App.class.getClassLoader().getResourceAsStream("config.json");
        Reader reader = new InputStreamReader(input, "UTF-8");
        Gson gson = new Gson();
        Config config = gson.fromJson(reader, Config.class);
//        Config config = new Config(
//                object.getString("jobcoinUrl"),
//                object.getString("transactionPath"),
//                object.getString("houseAddress")
//        );

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
    }
}
