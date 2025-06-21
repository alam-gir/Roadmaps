package com.roadmaps.Roadmaps.modules.roadmap.repository;

import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UpvoteRepository extends JpaRepository<Upvote, UUID> {
    List<Upvote> findAllByRoadmap(Roadmap roadmap);

    List<Upvote> findAllByComment(Comment comment);

    List<Upvote> findAllByComment_IdIn(List<UUID> allCommentIds);
}
