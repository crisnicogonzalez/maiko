package com.lemon.maiko.filter.service.impl;

import com.lemon.maiko.filter.service.RateLimitService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;


public class ApiRateLimitServiceConcurrentMapImplTest {

    private RateLimitService apiRateLimitService;
    private static final int QUANTITY_REQUEST_LIMIT = 5;


    @Before
    public void after() {
        apiRateLimitService = new ApiRateLimitServiceImpl(new UserAccessLogConcurrenceMapServiceImpl(), QUANTITY_REQUEST_LIMIT, new LocalLockServiceImpl());
    }

    @Test
    public void testOnlyFiveRequestWereExecutedSuccessfully() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        Boolean[] digits = new Boolean[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int pos = i;
            service.execute(() -> {
                digits[pos] = this.apiRateLimitService.userHaveReachedRateLimit("someUserApiId");
                latch.countDown();
            });
        }
        latch.await();

        List<Boolean> booleans = Arrays.asList(digits);

        long successCases = booleans.stream().filter(p -> !p).count();

        assertEquals(successCases, QUANTITY_REQUEST_LIMIT);
    }
}