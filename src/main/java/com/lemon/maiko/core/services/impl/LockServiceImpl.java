package com.lemon.maiko.core.services.impl;

import com.google.common.collect.MapMaker;
import com.lemon.maiko.core.services.LockService;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public class LockServiceImpl implements LockService {

    private final Map<String, Lock> lockByUserApiId;

    public LockServiceImpl() {
        lockByUserApiId = new MapMaker()
                .concurrencyLevel(4)
                .weakKeys()
                .makeMap();
    }

    @Override
    public void lock(String userApiId) {
        lockByUserApiId.get(userApiId).lock();
    }

    @Override
    public void unlock(String userApiId) {
        lockByUserApiId.get(userApiId).unlock();
    }
}
