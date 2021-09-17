package com.lemon.maiko.core.services;

import com.lemon.maiko.core.model.AccessLog;

public interface UserAccessLogService {

    AccessLog getUserAccessLog(String userApiId);

    void createAccessLogAndIncrementOne(String userApiId);

    void incrementCounterToOne(String userApiId);
}
