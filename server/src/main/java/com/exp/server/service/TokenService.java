package com.exp.server.service;

import com.exp.server.data.TokenRepository;
import com.exp.server.entity.AppUser;
import com.exp.server.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${security.token.accessTime}")
    private long accessTime;
    @Value("${security.token.refreshTime}")
    private long refreshTime;
    @Value("${security.appSecret}")
    private String secret;
    private final TokenRepository repository;

    public void saveRefreshToken(String refreshTokenValue, AppUser user) {
        var refreshToken = new Token();
        refreshToken.setTokenValue(refreshTokenValue);
        refreshToken.setRevoked(false);
        refreshToken.setAppUser(user);
        repository.save(refreshToken);
    }

    public Optional<Token> findByTokenValue(String tokenValue) {
        return repository.findFirstByTokenValue(tokenValue);
    }

    // можно удалять
    public void revokeTokenByValue(String tokenValue) {
        findByTokenValue(tokenValue).ifPresent(token -> {
            System.out.println(token.getId());
            token.setRevoked(true);
            repository.save(token);
        });
    }

    // можно удалять
    public void revokeAllTokensByUser(AppUser appUser) {
        List<Token> tokens = repository.findAllByAppUser(appUser);
        tokens.forEach(token -> token.setRevoked(true));
        repository.saveAll(tokens);
    }

    public String generateAccessToken(UserDetails user) {
        return generateToken(user, accessTime);
    }

    public String generateRefreshToken(AppUser user) {
        return generateToken(user, refreshTime);
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // надо бы тоже обернуть в try/catch
    public String extractLogin(String token) {
        return getClaims(token).getSubject();
    }

    private String generateToken(UserDetails userDetails, long time) {
        long currentDate = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(currentDate))
                .expiration(new Date(currentDate + time))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Может бросить ошибку в случае, если у токена вышел срок годности
     */
    private Claims getClaims(String token) throws JwtException, IllegalArgumentException{
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    // в зависимости от длины secret'a подбирает кодировку
    // возвращаем именно SecretKey, а не Key
    private SecretKey getSecretKey() {
        byte[] byteSecret = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(byteSecret);
    }

}
