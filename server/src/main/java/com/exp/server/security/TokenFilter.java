package com.exp.server.security;

import com.exp.server.enumeration.TokenType;
import com.exp.server.service.AppUserService;
import com.exp.server.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final AppUserService appUserService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String bearer = "%s ".formatted(TokenType.BEARER.getCode());
            if (Objects.nonNull(authHeader) && authHeader.startsWith(bearer)) {
                String token = authHeader.substring(bearer.length());
                String login;
                try {
                    login = tokenService.extractLogin(token);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                    return;
                }
                if (Objects.nonNull(login)
                        && !tokenService.isExpired(token)
                        && appUserService.findByLogin(login).isPresent()) {
                    UserDetails user = appUserService.getByLogin(login);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        if (401 != response.getStatus()) {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Игнорируем перечисленные урлы
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Set<String> whiteList = Set.of("/api/create", "/api/users");
        return whiteList.contains(request.getRequestURI());
    }

}
