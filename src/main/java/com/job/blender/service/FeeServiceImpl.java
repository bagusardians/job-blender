package com.job.blender.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FeeServiceImpl implements FeeService {
    BigDecimal cumulatedFees = BigDecimal.ZERO;
    private final Config config;

    public FeeServiceImpl(Config config) {
        this.config = config;
    }

    @Override
    public BigDecimal collectFee(BigDecimal originalAmount) {
        BigDecimal feesInDecimal = config.feesPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal feesCollected = originalAmount.multiply(feesInDecimal);
        cumulatedFees = cumulatedFees.add(feesCollected);
        return originalAmount.subtract(feesCollected);
    }

    @Override
    public BigDecimal getCumulatedFees() {
        return cumulatedFees;
    }
}
