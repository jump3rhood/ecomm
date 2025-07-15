package org.john.personal.userservice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.john.personal.userservice.models.AuthProvider;
import org.john.personal.userservice.models.Role;
import org.john.personal.userservice.models.UserAuthMethodEntity;
import org.john.personal.userservice.models.UserEntity;
import org.john.personal.userservice.repositories.UserAuthMethodRepository;
import org.john.personal.userservice.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;
    private final UserAuthMethodRepository userAuthMethodRepository;
    private final JwtEncoder jwtEncoder;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository, UserAuthMethodRepository userAuthMethodRepository, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.userAuthMethodRepository = userAuthMethodRepository;
        this.jwtEncoder = jwtEncoder;
    }


    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // Extract user info from google
        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String googleId = oauth2User.getAttribute("sub"); // Google's unique user ID

        // Handle null names with email prefix
        if(firstName == null || firstName.trim().isEmpty()){
            firstName = email.substring(0, email.indexOf("@"));
        }

        if(lastName == null || lastName.trim().isEmpty()){
            lastName = "";
        }

        System.out.println("OAuth2 Login - Email: " + email);
        System.out.println("OAuth2 Login - Google ID: " + googleId);

        try {
            // TODO: Create or find user in database
            UserEntity user = findOrCreateUser(email, firstName, lastName, googleId);

            // TODO: Generate JWT token
            String jwt = generateJwtForOAuth2User(user);

            // TODO: Handle response
            System.out.println("Generate JWT: " + jwt);
            response.sendRedirect("/oauth2/success?token=" + jwt);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("OAuth2 login failed: " + e.getMessage());
            response.sendRedirect("/oauth2/error?message=" + e.getMessage());
        }
    }
    private UserEntity findOrCreateUser(String email, String firstName, String lastName, String googleId) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        UserEntity user = null;
        UserAuthMethodEntity authMethod = null;

        if(userEntityOptional.isPresent()){
            user = userEntityOptional.get();
            // check if exisitng Google method, then update google Id
            Optional<UserAuthMethodEntity> userAuthMethodEntityOptional =
                    userAuthMethodRepository.findByUserAndProvider(user, AuthProvider.GOOGLE);
            if(userAuthMethodEntityOptional.isPresent()){
                authMethod = userAuthMethodEntityOptional.get();
                authMethod.setProviderId(googleId);
            }
        } else { // create new user
            user = new UserEntity();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            List<Role> roles = new ArrayList<>(List.of(Role.USER));
            user.setRoles(roles);
            // user password will be null
            user = userRepository.save(user);
        }

        if(authMethod == null){
            authMethod = new UserAuthMethodEntity();
            authMethod.setProvider(AuthProvider.GOOGLE);
            authMethod.setProviderId(googleId);
            authMethod.setUser(user);
        }
        authMethod = userAuthMethodRepository.save(authMethod);
        return user;
    }

    private String generateJwtForOAuth2User(UserEntity user) {
        // create authorities
        Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        Instant now = Instant.now();
        long expiryDuration = 3600;
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("User-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryDuration))
                .subject(user.getEmail())
                .claim("roles", user.getRoles().stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toSet())
                )
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("userId", user.getId())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
