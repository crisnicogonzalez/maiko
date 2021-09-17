package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.UserAccessLogService;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserAccessLogConcurrenceMapServiceImpl implements UserAccessLogService {

    private final Map<String, AccessLog> accessLogsByUserId;

    public UserAccessLogConcurrenceMapServiceImpl() {
        this.accessLogsByUserId = new ConcurrentHashMap<>();
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLogWithInitialValue(String userApiId, Integer initialValue) {
        this.accessLogsByUserId.compute(userApiId, (k, v) -> v == null ? AccessLog.builder().firstAccess(OffsetDateTime.now()).quantity(1).build() : AccessLog.builder().firstAccess(v.getFirstAccess()).quantity(v.getQuantity() + 1).build());
    }

    @Override
    public void incrementCounterToOne(String userApiId) {
        this.accessLogsByUserId.computeIfPresent(userApiId, ((s, c) -> {
            return AccessLog.builder().firstAccess(c.getFirstAccess()).quantity(c.getQuantity() + 1).build();
        }));
    }
}
