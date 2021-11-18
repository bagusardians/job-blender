package com.job.blender.service;

public class CoinPoller implements Runnable {
    private final PollService pollService;

    public CoinPoller() {
        this.pollService = null;
    }

    @Override
    public void run() {
        System.out.println("polling");
        //pollService.poll();
    }
}
