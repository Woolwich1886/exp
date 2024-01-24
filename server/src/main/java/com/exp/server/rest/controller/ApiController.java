package com.exp.server.rest.controller;

import com.exp.server.enumeration.AppRole;
import com.exp.server.rest.dto.AppUserCreationDTO;
import com.exp.server.rest.dto.StringValueDTO;
import com.exp.server.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiController {

    private final AppUserService appUserService;

    @GetMapping("/access/admin")
    public ResponseEntity<StringValueDTO> getAdminInfo() {
        var result = getInfoResult(AppRole.ADMIN);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/access/user")
    public ResponseEntity<StringValueDTO> getUserInfo() {
        var result = getInfoResult(AppRole.USER);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/access/reader")
    public ResponseEntity<StringValueDTO> getReaderInfo() {
        var result = getInfoResult(AppRole.READER);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<StringValueDTO> getAllUsers() {
        StringValueDTO result = StringValueDTO.build(appUserService.getAllUserDataAsString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<StringValueDTO> create(@RequestBody AppUserCreationDTO dto) {
        appUserService.createAppUser(dto);
        StringValueDTO result = StringValueDTO.build(appUserService.getAllUserDataAsString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private StringValueDTO getInfoResult(AppRole accessLevel) {
        return StringValueDTO.build("Есть доступ уровня = %s".formatted(accessLevel.name()));
    }

}
