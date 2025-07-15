package org.john.personal.userservice.services;

import org.john.personal.userservice.models.UserEntity;
import org.john.personal.userservice.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional
                = userRepository.findByUsername(username);
        if(userEntityOptional.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        UserEntity userEntity = userEntityOptional.get();
        return new User(userEntity.getUsername(), userEntity.getPassword(),
                userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList()));
    }

}
