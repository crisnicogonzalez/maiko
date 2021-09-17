package com.lemon.maiko.core.services;

public interface LockService {


    void lock(String userApiId);

    void unlock(String userApiId);
}
