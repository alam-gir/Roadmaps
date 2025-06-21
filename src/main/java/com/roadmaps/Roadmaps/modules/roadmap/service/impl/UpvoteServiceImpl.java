package com.roadmaps.Roadmaps.modules.roadmap.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import com.roadmaps.Roadmaps.modules.roadmap.repository.UpvoteRepository;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import com.roadmaps.Roadmaps.modules.roadmap.service.RoadmapService;
import com.roadmaps.Roadmaps.modules.roadmap.service.UpvoteService;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpvoteServiceImpl implements UpvoteService {
    private final UpvoteRepository upvoteRepository;
    private final RoadmapService roadmapService;
    private final UserService userService;
    private final CommentService commentService;

    // Roadmap upvotes
    @Override
    public List<Upvote> getUpvotesByRoadmapId(UUID roadmapId) {
        try{
            Roadmap roadmap = roadmapService.getById(roadmapId);
            return upvoteRepository.findAllByRoadmap(roadmap);
        } catch (ApiException ex) {
            log.warn("Error to finding upvotes by roadmap id : {}",  roadmapId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to get upvotes by roadmap id : {}",  roadmapId, ex);
            throw new ApiException("Failed to get roadmap upvotes.");
        }
    }

    @Override
    @Transactional
    public List<Upvote> upvoteByUserEmailAndRoadmapId(String userEmail, String roadmapId) {
        try{
            UUID roadmapUuid = UUID.fromString(roadmapId);
            User user = userService.getUserByEmail(userEmail);
            Roadmap roadmap = roadmapService.getById(roadmapId);

            List<Upvote> upvotes = getUpvotesByRoadmapId(roadmapUuid);

            if(
                    upvotes == null
                    || upvotes.isEmpty()
                    || haveNotVoted(user, upvotes)
            ){
                addRoadmapUpvote(user, roadmap);
            } else {
                removeUpvote(findUserUpvote(user, upvotes));
            }

            return upvoteRepository.findAllByRoadmap(roadmap);
        } catch (ApiException ex) {
            log.error("Failed to upvote to roadmap id : {}",  roadmapId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to get upvotes by roadmap id : {}", roadmapId, ex);
            throw new ApiException("Failed to upvotes. Try again!");
        }
    }

    // Comment upvotes
    @Override
    public List<Upvote> getUpvotesByCommentId(UUID commentId) {
        try{
            Comment comment = commentService.getById(commentId);

            return upvoteRepository.findAllByComment(comment);
        } catch (ApiException ex) {
            log.warn("Error to finding upvotes by comment id : {}",  commentId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to get upvotes by comment id : {}",  commentId, ex);
            throw new ApiException("Failed to get comment upvotes.");
        }
    }

    @Override
    public List<Upvote> upvoteByUserEmailAndCommentId(String userEmail, String commentId) {
        try{
            UUID commentUuid = UUID.fromString(commentId);
            User user = userService.getUserByEmail(userEmail);
            Comment comment = commentService.getById(commentId);

            List<Upvote> upvotes = getUpvotesByCommentId(commentUuid);

            if(
                    upvotes == null
                    || upvotes.isEmpty()
                    || haveNotVoted(user, upvotes)
            ){
                addCommentUpvote(user, comment);
            } else {
                removeUpvote(findUserUpvote(user, upvotes));
            }

            return upvoteRepository.findAllByComment(comment);
        } catch (ApiException ex) {
            log.error("Failed to upvote to comment id : {}",  commentId, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to get upvotes by comment id : {}", commentId, ex);
            throw new ApiException("Failed to upvotes. Try again!");
        }
    }

    // helper methods for both - roadmap upvotes and comment upvotes.
    private boolean haveNotVoted(User user, List<Upvote> upvotes) {
        return findUserUpvote(user, upvotes) == null;
    }

    private Upvote findUserUpvote(User user, List<Upvote> upvotes) {
        return upvotes.stream().filter(upvote ->
                upvote.getUser() != null && upvote.getUser().getId().equals(user.getId())
        ).findFirst().orElse(null);
    }

    private void removeUpvote(Upvote upvote) {
        try {
            upvoteRepository.delete(upvote);
        } catch (Exception ex) {
            log.error("Failed to delete upvote : {}",  upvote.getId(), ex);
            throw new ApiException("Failed to downvote!");
        }
    }

    private void addRoadmapUpvote(User user, Roadmap roadmap) {
        Upvote upvote = Upvote.builder()
                .user(user)
                .roadmap(roadmap)
                .build();

        upvoteRepository.save(upvote);
    }

    private void addCommentUpvote(User user, Comment comment) {
        Upvote upvote = Upvote.builder()
                .user(user)
                .comment(comment)
                .build();

        upvoteRepository.save(upvote);
    }

}
