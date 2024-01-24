package com.exp.server;

import com.exp.server.data.AppUserRepository;
import com.exp.server.entity.AppUser;
import com.exp.server.enumeration.AppRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.exp.server.data")
@EntityScan(basePackages = "com.exp.server.entity")
@RequiredArgsConstructor
public class ServerApplication {

    private final AppUserRepository appUserRepository;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    // добавляем в базу несколько пользователей на старте приложения
    @PostConstruct
    private void init() {
        var admin = new AppUser();
        admin.setLogin("admin");
        admin.setPassword("111");
        admin.setRole(AppRole.ADMIN);
        appUserRepository.save(admin);

        var user = new AppUser();
        user.setLogin("user");
        user.setPassword("11");
        user.setRole(AppRole.USER);
        appUserRepository.save(user);

        var reader = new AppUser();
        reader.setLogin("reader");
        reader.setPassword("1");
        reader.setRole(AppRole.READER);
        appUserRepository.save(reader);
    }
}
