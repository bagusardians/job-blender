package com.job.blender.service;

import java.math.BigDecimal;

public class Transaction {
    String fromAddress;
    String toAddress;
    BigDecimal amount;

    public Transaction(String fromAddress, String toAddress, BigDecimal amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }
}
