package com.lemon.maiko.client.impl;

import com.lemon.maiko.api.foaas.res.Operation;
import com.lemon.maiko.client.FoaasClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FoaasClientImpl implements FoaasClient {


    private final String host;
    private static final String OPERATIONS_ENDPOINT = "/operations";
    private static final Logger LOGGER = LoggerFactory.getLogger(FoaasClientImpl.class);


    public FoaasClientImpl(String host) {
        this.host = host;
    }

    @Override
    public List<Operation> getOperations() {
        return null;
    }
}
