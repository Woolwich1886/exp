package com.exp.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "token")
public class Token extends BaseEntity {

    /**
     * Строковое значение токена
     */
    @Column(name = "token_value", nullable = false)
    private String tokenValue;

    /**
     * Свяазнный пользователь
     */
    @Column(name = "app_user_id", nullable = false)
    private AppUser appUser;

    /**
     * Заблочен или нет
     */
    @Column(name = "is_expired", nullable = false)
    private boolean isRevoked;

}
