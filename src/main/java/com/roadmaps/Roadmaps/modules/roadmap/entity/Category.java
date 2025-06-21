package com.roadmaps.Roadmaps.modules.roadmap.entity;

import com.roadmaps.Roadmaps.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "categories")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Roadmap> roadmaps;
}
