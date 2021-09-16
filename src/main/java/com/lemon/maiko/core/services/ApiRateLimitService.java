package com.lemon.maiko.core.services;

public interface ApiRateLimitService {

    boolean userHaveReachedRateLimit(String userApi);
}
