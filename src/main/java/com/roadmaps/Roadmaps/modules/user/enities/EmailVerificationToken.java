package com.roadmaps.Roadmaps.modules.user.enities;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken extends BaseEntity {
    @Column(nullable = false)
    String token;

    @Column(nullable = false)
    LocalDateTime expiredAt;
}
