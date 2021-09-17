package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.services.ApiRateLimitService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;


public class ApiRateLimitServiceImplTest {

    private ApiRateLimitService apiRateLimitService;


    @Before
    public void after() {
        apiRateLimitService = new ApiRateLimitServiceImpl(new UserAccessLogServiceImpl(), 5);
    }

    @Test
    public void testOnlyNRequests() throws InterruptedException {
        long successExecutionsTimes = this.parallelCall(this.apiRateLimitService, 10);
        Assert.assertEquals(successExecutionsTimes, Integer.valueOf(5).longValue());
    }


    private long parallelCall(ApiRateLimitService apiRateLimitService,
                              int executionTimes) {

        return IntStream.of(executionTimes)
                .boxed()
                .map(i -> {
                    return apiRateLimitService.userHaveReachedRateLimit("1");
                })
                .filter(r -> r)
                .count();
    }
}