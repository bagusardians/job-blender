package com.job.blender.service;

import com.grack.nanojson.JsonParserException;

import java.io.IOException;

public class CoinPoller implements Runnable {
    private final PollService pollService;

    public CoinPoller(PollService pollService) {
        this.pollService = pollService;
    }

    @Override
    public void run() {
        try {
            pollService.poll();
        } catch (JsonParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
