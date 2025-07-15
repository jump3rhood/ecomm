package org.john.personal.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_auth_methods", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "provider"})
})
@NoArgsConstructor
@Getter
@Setter
public class UserAuthMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name="provider", nullable=false)
    private AuthProvider provider;

    @Column(name="provider_id")
    private String providerId; // null for Local
}
