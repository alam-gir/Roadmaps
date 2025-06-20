package com.roadmaps.Roadmaps.modules.roadmap.service.impl;

import com.roadmaps.Roadmaps.cache.CommentCacheService;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.repository.CommentRepository;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentCacheService commentCacheService;

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

    private Comment getCommentById(UUID id) {
        try{
            Comment comment = commentCacheService.getById(id.toString());
            if(comment == null){
                comment = commentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Comment Not Found!"));
            }

            commentCacheService.setById(id.toString(), comment);

            return comment;
        } catch (ApiException | NotFoundException e) {
            log.warn("Failed to find comment : {}", e.getMessage(), e);
            throw e;
        }
        catch (Exception ex) {
            log.error("Failed to find comment : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to find comment");
        }
    }
}
