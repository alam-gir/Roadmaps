package com.roadmaps.Roadmaps.modules.roadmap.entity;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Length;

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
