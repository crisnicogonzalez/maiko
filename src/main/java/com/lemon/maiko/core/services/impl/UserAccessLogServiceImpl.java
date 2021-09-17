package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.UserAccessLogService;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserAccessLogServiceImpl implements UserAccessLogService {

    private final Map<String, AccessLog> accessLogsByUserId;

    public UserAccessLogServiceImpl() {
        this.accessLogsByUserId = new ConcurrentHashMap<>();
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLogAndIncrementOne(String userApiId) {
        this.accessLogsByUserId.put(userApiId, AccessLog.builder().firstAccess(OffsetDateTime.now()).quantity(1).build());
    }

    @Override
    public void incrementCounterToOne(String userApiId) {
        this.accessLogsByUserId.get(userApiId).plusQuantityToOne();
    }
}
