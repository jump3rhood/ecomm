package org.john.personal.userservice.services;

import org.john.personal.userservice.dtos.request.RegisterRequestDto;
import org.john.personal.userservice.dtos.response.UserResponseDTO;
import org.john.personal.userservice.exceptions.CustomAuthenticationException;
import org.john.personal.userservice.exceptions.UserNameAlreadyExists;
import org.john.personal.userservice.models.Role;
import org.john.personal.userservice.models.UserEntity;
import org.john.personal.userservice.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    public UserResponseDTO registerUser(RegisterRequestDto requestDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(requestDto.getEmail());
        if(userEntityOptional.isPresent()) {
            throw new UserNameAlreadyExists("Username already taken!");
        }
        UserEntity user = new UserEntity();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        if(requestDto.getRoles() != null && !requestDto.getRoles().isEmpty()){
            System.out.println(requestDto.getRoles());
            user.setRoles(new ArrayList<>(requestDto.getRoles()));
        } else {
            user.setRoles(List.of(Role.USER));
        }

        System.out.println("SAVING NEW USER==========");
        user = userRepository.save(user);
        return UserResponseDTO.from(user);
    }

    public Map<String, Object> login(String email, String password) {

        Map<String, Object> response = new HashMap<>();

        System.out.println("Reached here*****************");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            // Only fetch user after successful authentication
            Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
            if(userEntityOptional.isEmpty())
                    throw new RuntimeException("User found during auth but not in DB - data inconsistency!");
            String accessToken = generateToken(userEntityOptional.get(), authentication, 3600);
            response.put("access_token", accessToken);
            response.put("expires_in", 3600);
            return response;
        }catch(UsernameNotFoundException | BadCredentialsException e) {
            throw new CustomAuthenticationException("Bad credentials. Invalid username or password!");
        }
    }

    private String generateToken(UserEntity userEntity, Authentication authentication, long expiryDuration){
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("User-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryDuration))
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .claim("firstName", userEntity.getFirstName())
                .claim("lastName", userEntity.getLastName())
                .claim("userId", userEntity.getId())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        for(UserEntity user : allUsers) {
            System.out.println(user.getEmail());
        }
        return allUsers.stream().map(UserResponseDTO::from).collect(Collectors.toList());
    }

}
