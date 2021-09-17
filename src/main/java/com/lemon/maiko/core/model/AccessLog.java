package com.lemon.maiko.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog implements Serializable {

    private OffsetDateTime firstAccess;
    private Integer currentQuantity;


    public void plusQuantityToOne() {
        currentQuantity = currentQuantity + 1;
    }
}
