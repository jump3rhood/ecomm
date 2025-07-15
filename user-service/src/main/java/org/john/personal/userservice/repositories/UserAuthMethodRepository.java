package org.john.personal.userservice.repositories;

import org.john.personal.userservice.models.AuthProvider;
import org.john.personal.userservice.models.UserAuthMethodEntity;
import org.john.personal.userservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthMethodRepository extends JpaRepository<UserAuthMethodEntity, Long> {
    Optional<UserAuthMethodEntity> findByUserAndProvider(UserEntity user, AuthProvider provider);
    Optional<UserAuthMethodEntity> findByProviderAndProviderId(AuthProvider provider, String providerId);
    boolean existsByUserAndProvider(UserEntity user, String provider);

    UserAuthMethodEntity save(UserAuthMethodEntity userAuthMethod);
}
