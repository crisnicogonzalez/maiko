package com.lemon.maiko.core.services.impl;

import com.google.common.collect.MapMaker;
import com.lemon.maiko.core.services.LockService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class RedisLockServiceImpl implements LockService {

    private final Map<String, Lock> lockByUserApiId;
    private final RedissonClient redisson;

    public RedisLockServiceImpl() {
        lockByUserApiId = new MapMaker()
                .concurrencyLevel(10)
                .weakKeys()
                .makeMap();

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redisson = Redisson.create(config);
    }

    @Override
    public void lock(String userApiId) {
        lockByUserApiId.computeIfAbsent(userApiId, key -> redisson.getLock(userApiId)).lock();
    }

    @Override
    public void unlock(String userApiId) {
        lockByUserApiId.get(userApiId).unlock();
    }
}
