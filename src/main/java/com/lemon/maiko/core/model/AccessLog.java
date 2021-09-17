package com.lemon.maiko.core.model;


import lombok.Getter;

import java.time.OffsetDateTime;


@Getter
public class AccessLog {

    private final OffsetDateTime firstAccess;
    private int quantity;


    public AccessLog() {
        firstAccess = OffsetDateTime.now();
        quantity = 0;
    }

    public void plusQuantityToOne() {
        quantity = quantity + 1;
    }
}
