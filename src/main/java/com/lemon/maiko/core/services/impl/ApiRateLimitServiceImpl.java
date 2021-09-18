package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.ApiRateLimitService;
import com.lemon.maiko.core.services.LockService;
import com.lemon.maiko.core.services.UserAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class ApiRateLimitServiceImpl implements ApiRateLimitService {


    private static final Integer TIME_LIMIT_IN_SECONDS = 10;
    private static final Integer TIMES_INITIAL_VALUE = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRateLimitServiceImpl.class);
    private final Integer quantityRequestLimit;
    private final UserAccessLogService accessLogService;
    private final LockService lockService;


    public ApiRateLimitServiceImpl(UserAccessLogService accessLogService, Integer quantityRequestLimit, LockService lockService) {
        this.accessLogService = accessLogService;
        this.quantityRequestLimit = quantityRequestLimit;
        this.lockService = lockService;
    }

    @Override
    public boolean userHaveReachedRateLimit(String userApiId) {
        LOGGER.info("Check if user {} have reached limit", userApiId);
        this.lockService.lock(userApiId);
        try {
            final AccessLog log = this.accessLogService.getUserAccessLog(userApiId);
            if (this.isFirstTimeOrWindowTimeShouldBeReset(log)) {
                LOGGER.info("Create new log {}", userApiId);
                this.accessLogService.createAccessLogWithInitialValue(userApiId, TIMES_INITIAL_VALUE);
                return false;
            } else {
                int quantity = log.getCurrentQuantity();
                LOGGER.info("Current quantity {}", quantity);
                if (quantity < this.quantityRequestLimit) {
                    this.accessLogService.incrementCounterToOne(userApiId);
                    return false;
                }
                return true;
            }
        } finally {
            this.lockService.unlock(userApiId);
        }

    }

    protected Integer getRequestQuantity(String userApiId) {
        return this.accessLogService.getUserAccessLog(userApiId).getCurrentQuantity();
    }

    protected OffsetDateTime getFirstAccess(String userApiId) {
        return this.accessLogService.getUserAccessLog(userApiId).getFirstAccess();
    }


    private boolean isFirstTimeOrWindowTimeShouldBeReset(AccessLog log) {
        if (log == null) {
            return true;
        }
        final OffsetDateTime oldestLimit = OffsetDateTime.now().minusSeconds(TIME_LIMIT_IN_SECONDS);
        final OffsetDateTime firstAccess = log.getFirstAccess();
        LOGGER.info("First access {} oldestLimit {}", firstAccess, oldestLimit);
        return firstAccess.isBefore(oldestLimit);
    }
}
