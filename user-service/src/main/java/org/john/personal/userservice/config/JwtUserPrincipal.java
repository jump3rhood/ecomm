package org.john.personal.userservice.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class JwtUserPrincipal implements UserDetails {

    private final String username; // email
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUserPrincipal(String username, Long userId, String firstName, String lastName, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // jwt has no password
    }

    @Override
    public String getUsername() {
        return username;
    }

}
