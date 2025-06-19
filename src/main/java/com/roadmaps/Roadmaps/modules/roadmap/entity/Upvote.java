package com.roadmaps.Roadmaps.modules.roadmap.entity;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "upvotes")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Upvote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "roadmap_id"
    )
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "comment_id"
    )
    private Comment comment;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if((this.roadmap != null && this.comment != null) || (this.roadmap == null && this.comment == null)){
            throw new ApiException("Upvote can be either for a roadmap or a comment but not both.");
        }
    }
}
