package com.exp.server.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {
    private String refresh_token;
    private String grant_type;
}
