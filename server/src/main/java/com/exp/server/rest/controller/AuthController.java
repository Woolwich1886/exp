package com.exp.server.rest.controller;

import com.exp.server.rest.dto.CredentialsDTO;
import com.exp.server.rest.dto.TokenDTO;
import com.exp.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    // отсутсвует @RequestBody
    // https://stackoverflow.com/questions/33796218/content-type-application-x-www-form-urlencodedcharset-utf-8-not-supported-for
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(CredentialsDTO dto) {
        TokenDTO result = authService.authenticate(dto.getUsername(), dto.getPassword());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokenDTO> refreshToken(@RequestBody TokenDTO tokenDTO) {
        TokenDTO result = authService.refreshToken(tokenDTO.getRefreshToken());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
