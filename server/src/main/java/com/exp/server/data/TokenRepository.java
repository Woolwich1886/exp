package com.exp.server.data;

import com.exp.server.entity.AppUser;
import com.exp.server.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findFirstByTokenValue(String tokenValue);

    List<Token> findAllByAppUser(AppUser appUser);

}
