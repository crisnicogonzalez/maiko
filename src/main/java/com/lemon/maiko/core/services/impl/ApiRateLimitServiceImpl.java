package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.ApiRateLimitService;
import com.lemon.maiko.core.services.UserAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class ApiRateLimitServiceImpl implements ApiRateLimitService {

    private final UserAccessLogService accessLogService;

    private static final Integer TIME_LIMIT_IN_SECONDS = 10;
    private static final Integer QUANTITY_REQUEST_LIMIT = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRateLimitServiceImpl.class);


    public ApiRateLimitServiceImpl(UserAccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public boolean userHaveReachedRateLimit(String userApiId) {
        final AccessLog log = this.accessLogService.getUserAccessLog(userApiId);
        if (log == null) {
            LOGGER.info("First access for user {}", userApiId);
            this.accessLogService.createAccessLog(userApiId);
            this.accessLogService.addNewAccess(userApiId);
            return false;
        }
        final OffsetDateTime oldestLimit = OffsetDateTime.now().minusSeconds(TIME_LIMIT_IN_SECONDS);
        final OffsetDateTime firstAccess = log.getFirstAccess();
        if (firstAccess.isBefore(oldestLimit)) {
            LOGGER.info("Reset counter for user {}", userApiId);
            this.accessLogService.resetLogsAndAddNewAccess(userApiId);
            return false;
        } else {
            int quantity = log.getQuantity();
            LOGGER.info("Current quantity {}", quantity);
            if (quantity < QUANTITY_REQUEST_LIMIT) {
                log.plusQuantityToOne();
                return false;
            }
            return true;
        }
    }
}
