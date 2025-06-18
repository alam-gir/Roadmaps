package com.roadmaps.Roadmaps.modules.user.enities;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.modules.user.enumerations.ROLE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ROLE role = ROLE.USER;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "verification_token_id"
    )
    private EmailVerificationToken verificationToken;
}