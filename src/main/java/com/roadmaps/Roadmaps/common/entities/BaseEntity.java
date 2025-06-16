package com.roadmaps.Roadmaps.common.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Getter
@SuperBuilder
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    protected BaseEntity() { super();}
}