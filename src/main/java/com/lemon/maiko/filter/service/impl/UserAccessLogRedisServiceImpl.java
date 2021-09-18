package com.lemon.maiko.filter.service.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.filter.service.UserAccessLogService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import static java.time.OffsetDateTime.now;

public class UserAccessLogRedisServiceImpl implements UserAccessLogService {

    private final RMap<String, AccessLog> accessLogsByUserId;

    public UserAccessLogRedisServiceImpl(RedissonClient redisson) {
        this.accessLogsByUserId = redisson.getMap("ACCESS_LOG_MAP");
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLogWithInitialValue(String userApiId, Integer quantity) {
        this.accessLogsByUserId.fastPut(userApiId, AccessLog.builder().firstAccess(now()).currentQuantity(quantity).build());
    }

    @Override
    public void incrementCounterToOne(String userApiId) {
        this.accessLogsByUserId.computeIfPresent(userApiId, (k, v) -> AccessLog.builder().firstAccess(v.getFirstAccess()).currentQuantity(v.getCurrentQuantity() + 1).build());
    }
}
