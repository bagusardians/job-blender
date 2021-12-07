package com.job.blender.service;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class FeeServiceImplTest extends TestCase {
    Config config = new Config("", "", "", BigDecimal.valueOf(10));
    FeeService underTest = new FeeServiceImpl(config);

    public void testCollectFee_ThenCheckTheCumulatedFee_ItMatches() {
        BigDecimal newAmount = underTest.collectFee(BigDecimal.valueOf(100));
        assertEquals(0, BigDecimal.valueOf(90).compareTo(newAmount));
        assertEquals(0, BigDecimal.valueOf(10).compareTo(underTest.getCumulatedFees()));
    }
}