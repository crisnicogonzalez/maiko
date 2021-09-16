package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.UserAccessLogService;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserAccessLogServiceImpl implements UserAccessLogService {

    private final Map<String, AccessLog> accessLogsByUserId;

    public UserAccessLogServiceImpl() {
        this.accessLogsByUserId = new HashMap<>();
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLog(String userApiId) {
        this.accessLogsByUserId.put(userApiId, new AccessLog(5));
    }

    @Override
    public void addNewAccess(String userApiId) {
        this.accessLogsByUserId.get(userApiId).getAccess().add(OffsetDateTime.now());
    }
}
