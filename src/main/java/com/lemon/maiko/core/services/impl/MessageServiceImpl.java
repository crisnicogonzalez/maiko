package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.api.res.Message;
import com.lemon.maiko.client.FoaasClient;
import com.lemon.maiko.core.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final FoaasClient foaasClient;

    public MessageServiceImpl(FoaasClient foaasClient) {
        this.foaasClient = foaasClient;
    }

    @Override
    public String getMessage(String userApiId) {
        LOGGER.info("Get message");
        return this.foaasClient.getMessage(userApiId);
    }
}
