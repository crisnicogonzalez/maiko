package com.lemon.maiko.client.impl;

import com.lemon.maiko.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class JerseyRestClientImpl implements RestClient {
    private final Client client;
    private final String host;

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyRestClientImpl.class);


    public JerseyRestClientImpl(String host) {
        if (host == null) {
            throw new RuntimeException("Host cannot be null");
        }
        this.host = host;
        this.client = ClientBuilder.newClient();
    }


    @Override
    public <T> T get(String path, Class<T> type, MediaType mediaType) {
        final String url = host + path;
        LOGGER.info("GET {}", url);
        return client.target(host + path)
                .request(mediaType)
                .get(type);
    }
}
