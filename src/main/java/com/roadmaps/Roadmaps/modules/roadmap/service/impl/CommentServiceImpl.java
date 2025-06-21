package com.roadmaps.Roadmaps.modules.roadmap.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.common.r2Storage.R2StorageService;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.CommentRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.RoadmapMapper;
import com.roadmaps.Roadmaps.modules.roadmap.repository.CommentRepository;
import com.roadmaps.Roadmaps.modules.roadmap.repository.RoadmapRepository;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import com.roadmaps.Roadmaps.modules.user.enities.User;
import com.roadmaps.Roadmaps.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final R2StorageService r2StorageService;
    private final RoadmapMapper roadmapMapper;
    private final UserService userService;
    private final RoadmapRepository roadmapRepository;

    @Override
    public Comment getById(String id) {
        UUID uuid = null;
        try{
            uuid = UUID.fromString(id);
        } catch (Exception e){
            log.debug("Failed to generate uuid from string : {}", e.getMessage());
            throw new ApiException("Unexpected error in the server!");
        }
        return getById(uuid);
    }

    @Override
    public Comment getById(UUID id) {
        return getCommentById(id);
    }

    @Override
    public Comment addComment(String userEmail, UUID roadmapId, CommentRequestDto commentDto) {
        return createComment(userEmail, roadmapId, null, commentDto );
    }

    @Override
    public Comment addCommentReply(String userEmail, UUID roadmapId, UUID parentCommentId, CommentRequestDto commentDto) {
        return createComment(userEmail, roadmapId, parentCommentId, commentDto);
    }

    @Override
    @Transactional
    public void deleteUserComment(String userEmail, String commentId) {
        try{
            UUID commentUuid = UUID.fromString(commentId);
            Comment userComment = getByCommentIdAndUserEmail(commentUuid, userEmail);

            List<Comment> nestedComments = getAllNestedComments(commentUuid);

            // collect all images from comment and nested comments
            List<String> allImages = getImagesFromComments(nestedComments);
            // add root comment image
            if(userComment.getImage() != null)
                allImages.add(userComment.getImage());

            commentRepository.delete(userComment);
            r2StorageService.deleteAllFiles(allImages);
        } catch (NotFoundException ex) {
            log.error("Failed to delete comment by id : {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception e) {
            log.error("Failed to delete comment by id : {}", e.getMessage(), e);
            throw new ApiException("Failed to delete comment. Try again!");
        }
    }

    @Override
    public List<String> getAllImagesFromComments(List<Comment> comments) {
        List<Comment> allCommentIncludeNested = new ArrayList<>();

        for (Comment comment : comments) {
            allCommentIncludeNested.add(comment);
            collectNestedComments(comment.getId(), allCommentIncludeNested);
        }

        return getImagesFromComments(allCommentIncludeNested);
    }

    private Comment createComment(String userEmail, UUID roadmapId, UUID parentCommentId, CommentRequestDto commentDto) {
        String image = null;
        try{
            // if comment is empty, throw error.
            validateData(commentDto.getText(), commentDto.getImage());

            // get all the required data
            User user = userService.getUserByEmail(userEmail);
            Roadmap roadmap = roadmapRepository.findById(roadmapId)
                    .orElseThrow(() -> new NotFoundException("Roadmap not found!"));

            Comment parentComment = getParentComment(parentCommentId);
            image = uploadImage(commentDto.getImage(), "roadmap_comment_images");

            Comment newComment = roadmapMapper.toCommentEntity(user, roadmap, parentComment, commentDto.getText(), image);

            return commentRepository.save(newComment);
        } catch (ApiException ex) {
            deleteImageIfFailed(image);
            log.error("Failed to add roadmap : {}", ex.getMessage(), ex);
            throw ex;
        }
        catch (Exception ex) {
            deleteImageIfFailed(image);
            log.error("Error while create roadmap : {}",ex.getMessage(), ex);
            throw new ApiException("Failed to add roadmap");
        }
    }

    private Comment getCommentById(UUID id) {
        try{
            return commentRepository.findById(id)
                     .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        } catch (ApiException | NotFoundException e) {
            log.warn("Failed to find comment : {}", e.getMessage(), e);
            throw e;
        }
        catch (Exception ex) {
            log.error("Failed to find comment : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to find comment");
        }
    }

    private Comment getByCommentIdAndUserEmail(UUID commentId, String userEmail) {
        try{
            return commentRepository.findByIdAndUser_Email(commentId, userEmail)
                    .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
        } catch (ApiException | NotFoundException e) {
            log.warn("Failed to find comment : {}", e.getMessage(), e);
            throw e;
        }
        catch (Exception ex) {
            log.error("Failed to find comment : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to find comment");
        }
    }

    private void validateData(String text, MultipartFile image) {
        if((text == null || text.trim().isEmpty()) &&  (image == null || image.isEmpty())){
            throw new ApiException("Text or image is required!");
        }
    }

    private String uploadImage(MultipartFile image, String folder) {
        if(image == null || image.isEmpty()) return null;
        return r2StorageService.fileUpload(image, folder);
    }

    private void deleteImageIfFailed(String image) {
        if(image != null && !image.isEmpty()){
            r2StorageService.deleteFile(image);
        }
    }

    private Comment getParentComment(UUID parentCommentId) {
        if(parentCommentId == null) return null;
        return getById(parentCommentId);
    }

    public List<Comment> getAllNestedComments(UUID commentId) {
        List<Comment> comments = new ArrayList<>();
        collectNestedComments(commentId, comments);
        return comments;
    }

    private void collectNestedComments(UUID commentId, List<Comment> comments) {
        try{
            List<Comment> children = commentRepository.getCommentsByParentId(commentId);

            for(Comment childComment : children){
                comments.add(childComment);
                collectNestedComments(childComment.getId(), comments);
            }
        } catch (Exception e){
            log.error("Failed to collect nested comments by comment id : {}", e.getMessage(), e);
            throw new ApiException("Failed to collect nested comments. Try again!");
        }
    }

    public List<String> getImagesFromComments(List<Comment> nestedComments) {
        List<String> images = new ArrayList<>();

        for (Comment comment : nestedComments) {
            if(comment.getImage() != null){
                images.add(comment.getImage());
            }
        }

        return images;
    }
}
