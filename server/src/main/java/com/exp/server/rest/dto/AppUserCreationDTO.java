package com.exp.server.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserCreationDTO {

    private String username;
    private String password;
    private String role;

}
