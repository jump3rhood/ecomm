package org.john.personal.userservice.models;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "Administrator with full access"),
    USER("ROLE_USER", "Standard user with basic access");

    private final String authority;
    private final String description;

    Role(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    public static Role fromAuthority(String authority) {
        for(Role role: Role.values()) {
            if(role.authority.equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }

}
