package com.exp.server.rest.dto;

import com.exp.server.entity.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInfoDTO {
    private String tokenValue;
    private boolean isRevoked;
    private String userLogin;

    public static TokenInfoDTO toDTO(Token token) {
        var dto = new TokenInfoDTO();
        dto.setRevoked(token.isRevoked());
        dto.setTokenValue(token.getTokenValue());
        dto.setUserLogin(token.getAppUser().getLogin());
        return dto;
    }
}
