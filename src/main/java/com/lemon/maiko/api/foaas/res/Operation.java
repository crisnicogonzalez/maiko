package com.lemon.maiko.api.foaas.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Operation {
    private String name;
    private String url;
    private List<Field> fields;
}
