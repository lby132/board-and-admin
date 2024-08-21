package com.fastcampus.projectboardadmin.domain.constant;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    DEVELOPER("ROLE_DEVELOPER"),
    ADMIN("ROLE_ADMIN")
    ;

    private final String name;

    RoleType(String name) {
        this.name = name;
    }
}
