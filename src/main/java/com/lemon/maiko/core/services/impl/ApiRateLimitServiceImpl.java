package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.core.services.ApiRateLimitService;

public class ApiRateLimitServiceImpl implements ApiRateLimitService {

    @Override
    public boolean userHaveReachedRateLimit(String userApi) {
        return false;
    }
}
