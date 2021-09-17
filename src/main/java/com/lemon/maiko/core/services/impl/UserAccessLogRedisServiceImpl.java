package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.model.AccessLog;
import com.lemon.maiko.core.services.UserAccessLogService;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import static java.time.OffsetDateTime.now;

public class UserAccessLogRedisServiceImpl implements UserAccessLogService {

    private final RMap<String, AccessLog> accessLogsByUserId;
    private final RedissonClient redisson;

    public UserAccessLogRedisServiceImpl() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redisson = Redisson.create(config);
        this.accessLogsByUserId = redisson.getMap("ACCESS_LOG_MAP");
    }

    @Override
    public AccessLog getUserAccessLog(String userApiId) {
        return this.accessLogsByUserId.get(userApiId);
    }

    @Override
    public void createAccessLogWithInitialValue(String userApiId, Integer quantity) {
        this.accessLogsByUserId.fastPut(userApiId, AccessLog.builder().firstAccess(now()).quantity(quantity).build());
    }

    @Override
    public void incrementCounterToOne(String userApiId) {
        this.accessLogsByUserId.computeIfPresent(userApiId, (k, v) -> AccessLog.builder().firstAccess(v.getFirstAccess()).quantity(v.getQuantity() + 1).build());
    }
}
