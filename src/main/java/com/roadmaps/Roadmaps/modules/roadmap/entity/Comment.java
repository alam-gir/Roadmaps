package com.roadmaps.Roadmaps.modules.roadmap.entity;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    private String text;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_id"
    )
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "roadmap_id",
            nullable = false
    )
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Upvote> upvotes = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void validateCommentContent() {
        if(
                (this.text == null || this.text.trim().isEmpty())
                && (this.image == null || this.image.trim().isEmpty())
        ){
            throw new ApiException("Comment can't be empty!");
        }
    }
}
