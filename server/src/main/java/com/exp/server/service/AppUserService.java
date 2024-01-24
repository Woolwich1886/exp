package com.exp.server.service;

import com.exp.server.data.AppUserRepository;
import com.exp.server.entity.AppUser;
import com.exp.server.enumeration.AppRole;
import com.exp.server.rest.dto.AppUserCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    public Optional<AppUser> findByLogin(String login) {
        return repository.findFirstByLogin(login);
    }

    public AppUser getByLogin(String login) {
        return repository.getByLogin(login);
    }

    public void createAppUser(AppUserCreationDTO model) {
        var newAppUser = new AppUser();
        newAppUser.setLogin(model.getUsername());
        newAppUser.setPassword(model.getPassword());
        newAppUser.setRole(AppRole.valueOf(model.getRole()));
        repository.save(newAppUser);
    }

    public String getAllUserDataAsString() {
        return repository.findAll()
                .stream()
                .map(u -> "{ Логин: %s, Пароль: %s, Роль: %s }".formatted(u.getUsername(), u.getPassword(), u.getRole()))
                .collect(Collectors.joining(", "));
    }

}
