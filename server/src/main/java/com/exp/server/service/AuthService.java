package com.exp.server.service;

import com.exp.server.entity.AppUser;
import com.exp.server.entity.Token;
import com.exp.server.enumeration.TokenType;
import com.exp.server.rest.dto.TokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler {

    private final AppUserService appUserService;
    private final TokenService tokenService;

    public TokenDTO authenticate(String login, String password) {
        AppUser user = appUserService.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с логином '%s' не найден".formatted(login))
                );
        if (!user.getPassword().equals(password)) {
            throw new BadCredentialsException("Неверный пароль");
        }
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        tokenService.saveRefreshToken(refreshToken, user);

        var tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(accessToken);
        tokenDTO.setRefreshToken(refreshToken);
        return tokenDTO;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearer = "%s ".formatted(TokenType.BEARER.getCode());
        if (Objects.nonNull(authHeader) && authHeader.startsWith(bearer)) {
            String token = authHeader.substring(bearer.length());
            tokenService.revokeTokenByValue(token);
        }
    }

    public TokenDTO refreshToken(String refreshToken) {
        if (Objects.nonNull(refreshToken)) {
            Token oldRefreshToken = tokenService.findByTokenValue(refreshToken)
                    .orElseThrow(() -> new RuntimeException("Токена нет"));
            String login = tokenService.extractLogin(refreshToken);
            Optional<AppUser> optUser = appUserService.findByLogin(login);
            if (optUser.isPresent() && !tokenService.isExpired(refreshToken) && !oldRefreshToken.isRevoked()) {
                AppUser user = optUser.get();
                tokenService.revokeAllTokensByUser(optUser.get());
                String newAccessToken = tokenService.generateAccessToken(user);
                String newRefreshToken = tokenService.generateRefreshToken(user);
                tokenService.saveRefreshToken(refreshToken, user);
                TokenDTO tokenDTO = new TokenDTO();
                tokenDTO.setAccessToken(newAccessToken);
                tokenDTO.setRefreshToken(newRefreshToken);
                return tokenDTO;
            }
        }
        throw new RuntimeException("Что то пошло не так");
    }

}
