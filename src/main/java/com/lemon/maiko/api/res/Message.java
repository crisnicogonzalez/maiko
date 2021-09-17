package com.lemon.maiko.api.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Message {
    private String message;
    private OffsetDateTime tries;

}
