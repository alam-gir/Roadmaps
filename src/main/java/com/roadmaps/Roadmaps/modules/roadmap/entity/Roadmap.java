package com.roadmaps.Roadmaps.modules.roadmap.entity;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.modules.roadmap.enumeration.ROADMAP_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Length;

import java.util.List;

@Entity
@Table(name = "roadmaps")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Roadmap extends BaseEntity {
    @Column(length = Length.LONG)
    private String text;

    private String image;

    private ROADMAP_STATUS status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    @OneToMany(mappedBy = "roadmap",  fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "roadmap", fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Upvote> upvotes;

    @PrePersist
    @PreUpdate
    public void validateRoadmapContent() {
        if(
                (this.text == null || this.text.trim().isEmpty())
                        && (this.image == null || this.image.trim().isEmpty())
        ){
            throw new ApiException("Roadmap can't be empty!");
        }
    }
}
