package com.exp.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    BEARER("Bearer");

    private final String code;
}
