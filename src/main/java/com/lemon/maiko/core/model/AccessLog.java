package com.lemon.maiko.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog {

    private OffsetDateTime firstAccess;
    private Integer quantity;


    public void plusQuantityToOne() {
        quantity = quantity + 1;
    }
}
