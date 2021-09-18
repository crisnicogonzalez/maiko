package com.lemon.maiko.filter.service.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.filter.service.RateLimitService;
import com.lemon.maiko.filter.service.UserAccessLogService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiRateLimitServiceTest {

    private static final Integer QUANTITY_REQUEST_LIMIT = 5;
    private static final Integer ONE = 1;
    private static final String USER_API_ID = "someUserApiId";
    @Mock
    private UserAccessLogService userAccessLogService;
    private RateLimitService apiRateLimitService;


    @Before
    public void before() {
        initMocks(this);
        this.apiRateLimitService = new ApiRateLimitServiceImpl(userAccessLogService, QUANTITY_REQUEST_LIMIT, new LocalLockServiceImpl());
    }


    @Test
    public void testFirstAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(0).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }


    @Test
    public void testSecondAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(1).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }

    @Test
    public void testThirdAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(2).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }


    @Test
    public void testFourthAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(3).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }


    @Test
    public void testFifthAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(4).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }

    @Test
    public void testSixthAccessShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(now).currentQuantity(5).build());

        Assert.assertTrue(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }


    @Test
    public void testRequestAfterElevenSecondsOfFirstRequestShouldBeOk() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime elevenSecondsBeforeNow = now.minusSeconds(11);
        when(userAccessLogService.getUserAccessLog(USER_API_ID)).thenReturn(AccessLog.builder().firstAccess(elevenSecondsBeforeNow).currentQuantity(5).build());

        Assert.assertFalse(this.apiRateLimitService.userHaveReachedRateLimit(USER_API_ID));
    }


}
