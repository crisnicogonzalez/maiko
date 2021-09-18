package com.lemon.maiko.filter.service;

import com.lemon.maiko.core.model.AccessLog;

public interface UserAccessLogService {

    AccessLog getUserAccessLog(String userApiId);

    void createAccessLogWithInitialValue(String userApiId,Integer quantity);

    void incrementCounterToOne(String userApiId);
}
