package com.job.blender;

import com.job.blender.service.CoinPoller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        CoinPoller poller = new CoinPoller();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(poller, 0, 5, TimeUnit.SECONDS);
    }
}
