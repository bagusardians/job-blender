package com.job.blender.service;

import junit.framework.TestCase;
import okhttp3.OkHttpClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class DoleServiceImplTest extends TestCase {

    FeeService feeService = new FeeService() {
        @Override
        public BigDecimal collectFee(BigDecimal originalAmount) {
            return originalAmount;
        }

        @Override
        public BigDecimal getCumulatedFees() {
            return null;
        }
    };

    public void testDole_WithTreeDestinationAddress_DividedCorrectly() {
        Config config = new Config("", "house", "/any", BigDecimal.valueOf(10));
        DoleServiceImpl underTest = new DoleServiceImpl(config, new OkHttpClient(), feeService);
        List<String> destinationAddresses = Arrays.asList("address1", "address2", "address3");
        underTest.dole(BigDecimal.TEN, destinationAddresses);
        Queue<Transaction> queue = underTest.getQueue();
        Transaction first = queue.poll();
        Transaction second = queue.poll();
        Transaction third = queue.poll();
        assertEquals(0, BigDecimal.valueOf(3.33).compareTo(first.amount));
        assertEquals(0, BigDecimal.valueOf(3.33).compareTo(second.amount));
        assertEquals(0, BigDecimal.valueOf(3.34).compareTo(third.amount));
    }
}