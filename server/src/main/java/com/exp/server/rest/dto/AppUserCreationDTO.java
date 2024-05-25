package com.exp.server.rest.dto;

import com.exp.server.entity.AppUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserCreationDTO {

    private String username;
    private String password;
    private String role;

    public static AppUserCreationDTO toDTO(AppUser appUser) {
        var dto = new AppUserCreationDTO();
        dto.setUsername(appUser.getUsername());
        dto.setPassword(appUser.getPassword());
        dto.setRole(appUser.getRole().name());
        return dto;
    }

}
