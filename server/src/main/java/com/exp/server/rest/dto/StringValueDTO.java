package com.exp.server.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringValueDTO {
    private String value;

    public static StringValueDTO build(String value) {
        var result = new StringValueDTO();
        result.setValue(value);
        return result;
    }
}
