package com.roadmaps.Roadmaps.modules.user.enities;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.modules.user.enumerations.ROLE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id"
    )
    private List<EmailVerificationToken> verificationTokens = new ArrayList<>();

    public void addVerificationToken(EmailVerificationToken verificationToken){
        if(this.verificationTokens == null){ this.verificationTokens = new ArrayList<>();}
        verificationTokens.add(verificationToken);
    }
}