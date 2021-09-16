package com.lemon.maiko.core.services.impl;

import com.lemon.maiko.api.res.Message;
import com.lemon.maiko.core.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public Message getMessage() {
        LOGGER.info("Get message");
        return new Message("dsa", 1);
    }
}
