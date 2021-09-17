package com.lemon.maiko.core.services.impl;

import com.google.common.collect.MapMaker;
import com.lemon.maiko.core.services.LockService;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class RedisLockServiceImpl implements LockService {

    private final Map<String, Lock> lockByUserApiId;
    private final RedissonClient redisson;

    public RedisLockServiceImpl(RedissonClient redisson) {
        lockByUserApiId = new MapMaker()
                .concurrencyLevel(10)
                .weakKeys()
                .makeMap();

        this.redisson = redisson;
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
