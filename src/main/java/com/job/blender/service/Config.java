package com.job.blender.service;

import java.math.BigDecimal;

public class Config {
    String jobcoinUrl;
    String houseAddress;
    String transactionPath;
    BigDecimal feesPercentage;

    public Config(String jobcoinUrl, String houseAddress, String transactionPath, BigDecimal feesPercentage) {
        this.jobcoinUrl = jobcoinUrl;
        this.houseAddress = houseAddress;
        this.transactionPath = transactionPath;
        this.feesPercentage = feesPercentage;
    }
}
