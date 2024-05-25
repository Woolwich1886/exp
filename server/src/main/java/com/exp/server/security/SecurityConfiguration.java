package com.exp.server.security;

import com.exp.server.enumeration.AppRole;
import com.exp.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenFilter tokenFilter;
    private final AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(tokenFilter, BasicAuthenticationFilter.class)
                // отключаем кучу всего
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .servletApi(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        r -> r
                                .requestMatchers("/login", "/error").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                // auth
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/refresh-token").permitAll()
                                .requestMatchers("/auth/logout").authenticated()
                                // test api
                                .requestMatchers("/api/users").permitAll()
                                .requestMatchers("/api/create").permitAll()
                                .requestMatchers("/api/tokens").permitAll()
                                .requestMatchers("/api/access").authenticated()
                                .requestMatchers("/api/access/admin").hasRole(AppRole.ADMIN.name())
                                .requestMatchers("/api/access/user").hasAnyRole(AppRole.ADMIN.name(), AppRole.USER.name())
                                .requestMatchers("/api/access/reader").hasAnyRole(AppRole.ADMIN.name(), AppRole.USER.name(), AppRole.READER.name())
                                //
                                .anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler(authService)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return httpSecurity.build();
    }

}
