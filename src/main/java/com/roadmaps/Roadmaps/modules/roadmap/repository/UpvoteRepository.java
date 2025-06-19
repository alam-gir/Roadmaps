package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UpvoteRepository extends JpaRepository<Upvote, UUID> {
}
