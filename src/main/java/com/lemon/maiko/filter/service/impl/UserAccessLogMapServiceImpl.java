package com.lemon.maiko.filter.service.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.filter.service.UserAccessLogService;

import java.util.HashMap;
import java.util.Map;

import static java.time.OffsetDateTime.now;

public class UserAccessLogMapServiceImpl implements UserAccessLogService {

    private final Map<String, AccessLog> accessLogsByUserId;

    public UserAccessLogMapServiceImpl() {
        this.accessLogsByUserId = new HashMap<>();
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLogWithInitialValue(String userApiId, Integer quantity) {
        this.accessLogsByUserId.put(userApiId, AccessLog.builder().firstAccess(now()).currentQuantity(quantity).build());
    }

    @Override
    public void incrementCounterToOne(String userApiId) {
        this.accessLogsByUserId.get(userApiId).plusQuantityToOne();
    }
}
