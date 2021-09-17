package com.lemon.maiko.client.impl;

import com.lemon.maiko.client.FoaasClient;
import com.lemon.maiko.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

public class FoaasClientImpl implements FoaasClient {

    private final String host;
    private static final String COOL_ENDPOINT = "/cool/:from";
    private static final Logger LOGGER = LoggerFactory.getLogger(FoaasClientImpl.class);
    private final RestClient restClient;


    public FoaasClientImpl(String host, RestClient restClient) {
        this.host = host;
        this.restClient = restClient;
    }

    @Override
    public String getMessage(String userApiId) {
        String path = COOL_ENDPOINT.replace(":from", userApiId);
        return this.restClient.get(path, String.class, MediaType.TEXT_HTML_TYPE);
    }
}
