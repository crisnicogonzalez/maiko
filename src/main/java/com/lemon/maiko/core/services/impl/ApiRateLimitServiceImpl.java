package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.ApiRateLimitService;
import com.lemon.maiko.core.services.LockService;
import com.lemon.maiko.core.services.UserAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class ApiRateLimitServiceImpl implements ApiRateLimitService {

    private final UserAccessLogService accessLogService;
    private final LockService lockService;

    private static final Integer TIME_LIMIT_IN_SECONDS = 10;
    private static final Integer QUANTITY_REQUEST_LIMIT = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRateLimitServiceImpl.class);


    public ApiRateLimitServiceImpl(UserAccessLogService accessLogService, LockService lockService) {
        this.accessLogService = accessLogService;
        this.lockService = lockService;
    }

    @Override
    public boolean userHaveReachedRateLimit(String userApiId) {
        LOGGER.info("Check if user {} have reached limit", userApiId);
        final AccessLog log = this.accessLogService.getUserAccessLog(userApiId);
        if (this.isFirstTimeOrWindowTimeShouldBeReset(log)) {
            LOGGER.info("First access for user {}", userApiId);
            this.accessLogService.createAccessLogAndIncrementOne(userApiId);
            return false;
        } else {
            int quantity = log.getQuantity();
            LOGGER.info("Current quantity {}", quantity);
            if (quantity < QUANTITY_REQUEST_LIMIT) {
                this.accessLogService.incrementCounterToOne(userApiId);
                return false;
            }
            return true;
        }
    }


    private boolean isFirstTimeOrWindowTimeShouldBeReset(AccessLog log) {
        if (log == null) {
            return true;
        }
        final OffsetDateTime oldestLimit = OffsetDateTime.now().minusSeconds(TIME_LIMIT_IN_SECONDS);
        final OffsetDateTime firstAccess = log.getFirstAccess();
        return firstAccess.isBefore(oldestLimit);
    }
}
