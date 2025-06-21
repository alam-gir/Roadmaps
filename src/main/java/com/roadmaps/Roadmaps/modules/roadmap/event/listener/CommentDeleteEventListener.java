package com.roadmaps.Roadmaps.modules.roadmap.event.listener;

import com.roadmaps.Roadmaps.common.r2Storage.R2StorageService;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.event.CommentDeleteEvent;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import com.roadmaps.Roadmaps.modules.roadmap.service.UpvoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentDeleteEventListener{
    private final CommentService commentService;
    private final UpvoteService upvoteService;
    private final R2StorageService r2StorageService;

    @EventListener
    @Async
    public void handleCommentDeleteEvent(CommentDeleteEvent event){
        try {

            UUID commentId = event.getComment().getId();
            Comment comment = event.getComment();

            List<Comment> nestedComments = commentService.getAllNestedComments(commentId);

            List<UUID> allCommentIds = getAllCommentIds(nestedComments);

            // collect all images from comment and nested comments
            List<String> allImages = getImagesFromComments(nestedComments);
            if(comment.getImage() != null)
                allImages.add(comment.getImage());

            upvoteService.deleteAllUpvoteByCommentIds(allCommentIds);

            commentService.delete(comment);
            deleteAllImage(allImages);


        } catch (Exception ex) {
            log.error("On comment delete event failed to delete comment by id : {}", ex.getMessage(), ex);
        }
    }

    private List<String> getImagesFromComments(List<Comment> nestedComments) {
        List<String> images = new ArrayList<>();

        for (Comment comment : nestedComments) {
            if(comment.getImage() != null){
                images.add(comment.getImage());
            }
        }

        return images;
    }

    private void deleteAllImage(List<String> images) {
        for(String image : images){
            r2StorageService.deleteFile(image);
        }
    }

    private List<UUID> getAllCommentIds(List<Comment> nestedComments) {
        return nestedComments.stream().map(Comment::getId).toList();
    }

}
