package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.services.ApiRateLimitService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;


public class ApiRateLimitServiceNoConcurrentMapImplTest {

    private ApiRateLimitService apiRateLimitService;
    private static final Integer QUANTITY_REQUEST_LIMIT = 5;
    private static final Integer ONE = 1;
    private static final String USER_API_ID = "someUserApiId";


    @Before
    public void after() {
        apiRateLimitService = new ApiRateLimitServiceImpl(new UserAccessLogMapServiceImpl(), QUANTITY_REQUEST_LIMIT, new LocalLockServiceImpl());
    }

    @Test
    public void testOnlyFiveRequestWereExecutedSuccessfully() throws InterruptedException {
        int numberOfThreads = 10;
        final ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);
        Boolean[] requestResults = new Boolean[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int pos = i;
            service.execute(() -> {
                requestResults[pos] = this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID);
                latch.countDown();
            });
        }
        latch.await();

        List<Boolean> booleans = Arrays.asList(requestResults);

        long successCases = booleans.stream().filter(p -> p).count();

        assertEquals(successCases, QUANTITY_REQUEST_LIMIT.longValue());

    }


    @Test
    public void testOneRequestShouldBeOk() {
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), ONE);
    }


    @Test
    public void testFiveRequestsShouldBeOk() {
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);
    }


    @Test
    public void testFiveRequestsShouldBeOkConcurrently() throws InterruptedException {

        int numberOfThreads = 5;
        final ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);
        Boolean[] requestResults = new Boolean[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int pos = i;
            service.execute(() -> {
                requestResults[pos] = this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID);
                latch.countDown();
            });
        }
        latch.await();

        List<Boolean> booleans = Arrays.asList(requestResults);

        long successCases = booleans.stream().filter(p -> !p).count();

        assertEquals(QUANTITY_REQUEST_LIMIT.longValue(), successCases);
        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);
    }


    @Test
    public void testFiveRequestsShouldBeOkAndTheSixthNot() {
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));

        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);

        assertTrue(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));

        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);
    }


    @Test
    public void testFiveRequestsShouldBeOkAndTheSixthNotWaitToCloseTimeWindow() {
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
        assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));

        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);

        assertTrue(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));

        assertEquals(this.apiRateLimitService.getRequestQuantity(USER_API_ID), QUANTITY_REQUEST_LIMIT);
    }
}