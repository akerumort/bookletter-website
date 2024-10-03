package com.akerumort.userservice.entities.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;

@Schema(description = "Role enumeration for user roles")
public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_MODER, ROLE_ADMIN;

    @Override
    @Schema(description = "Authority of the role", example = "ROLE_USER")
    public String getAuthority() {
        return name();
    }
}
