package com.lemon.maiko.core.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@NoArgsConstructor
@Getter
@Builder
public class AccessLog {

    private Queue<OffsetDateTime> access;

    public AccessLog(int size) {
        this.access = new ArrayBlockingQueue<>(size);
        this.access.add(OffsetDateTime.now());
    }
}
