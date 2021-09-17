package com.lemon.maiko.client;

import javax.ws.rs.core.MediaType;

public interface RestClient {
    <T> T get(String url, Class<T> classType, MediaType mediaType);
}
