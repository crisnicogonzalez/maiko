package com.lemon.maiko.filter.service;

public interface LockService {


    void lock(String userApiId);

    void unlock(String userApiId);
}
