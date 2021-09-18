package com.lemon.maiko.filter.service;

public interface RateLimitService {

    boolean userHaveReachedRateLimit(String userApi);

}
