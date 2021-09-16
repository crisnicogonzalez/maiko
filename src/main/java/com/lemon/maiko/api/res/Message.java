package com.lemon.maiko.api.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String message;
    private Integer tries;

    public Message() {
    }

    public Message(String message, Integer tries) {
        this.message = message;
        this.tries = tries;
    }


    @JsonProperty
    public Integer getTries() {
        return tries;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }


}
