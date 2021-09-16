package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.ApiRateLimitService;
import com.lemon.maiko.core.services.UserAccessLogService;

import java.time.OffsetDateTime;
import java.util.Queue;

public class ApiRateLimitServiceImpl implements ApiRateLimitService {

    private UserAccessLogService accessLogService;

    private static final Integer TIME_LIMIT_IN_SECONDS = 10;

    public ApiRateLimitServiceImpl(UserAccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public boolean userHaveReachedRateLimit(String userApi) {
        final AccessLog log = this.accessLogService.getUserAccessLog(userApi);
        if (log == null) {
            this.accessLogService.createAccessLog(userApi);
            return false;
        }
        final Queue<OffsetDateTime> access = log.getAccess();
        final OffsetDateTime nowMinusNSeconds = OffsetDateTime.now().minusSeconds(TIME_LIMIT_IN_SECONDS);
        final OffsetDateTime oldestAccess = access.element();
        if (nowMinusNSeconds.isAfter(oldestAccess)) {
            this.accessLogService.addNewAccess(userApi);
            return false;
        }
        return true;
    }
}
