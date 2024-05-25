package com.exp.server.data;

import com.exp.server.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findFirstByLogin(String login);

    AppUser getByLogin(String login);

}
