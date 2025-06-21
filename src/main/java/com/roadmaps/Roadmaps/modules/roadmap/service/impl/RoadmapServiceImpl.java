package com.roadmaps.Roadmaps.modules.roadmap.service.impl;

import com.roadmaps.Roadmaps.cache.RoadmapCacheService;
import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.common.r2Storage.R2StorageService;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Comment;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.RoadmapMapper;
import com.roadmaps.Roadmaps.modules.roadmap.repository.RoadmapRepository;
import com.roadmaps.Roadmaps.modules.roadmap.service.CommentService;
import com.roadmaps.Roadmaps.modules.roadmap.service.RoadmapService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {
    private final RoadmapMapper  roadmapMapper;
    private final RoadmapRepository   roadmapRepository;
    private final R2StorageService r2StorageService;
    private final RoadmapCacheService roadmapCacheService;
    private final CommentService commentService;

    @Override
    public Roadmap getById(String id) {
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
    public Roadmap getById(UUID id) {
        return getRoadmapById(id);
    }

    private Roadmap getRoadmapById(UUID id) {
        try{
            Roadmap roadmap = roadmapCacheService.getById(id.toString());
            if(roadmap == null){
                roadmap = roadmapRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Roadmap Not Found!"));
            }

            roadmapCacheService.setById(id.toString(), roadmap);

            return roadmap;
        } catch (ApiException | NotFoundException e) {
            log.warn("Failed to find roadmap : {}", e.getMessage(), e);
            throw e;
        }
        catch (Exception ex) {
            log.error("Failed to find roadmap : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to find roadmap");
        }
    }

    @Override
    @Transactional
    public Roadmap addRoadmap(RoadmapRequestDto roadmapDto) {
        String image = null;
        try{
            // if roadmap is empty, throw error.
            validateData(roadmapDto.getText(), roadmapDto.getImage());

            image = uploadImage(roadmapDto.getImage(), "roadmap_images");

            Roadmap roadmap = roadmapMapper.toEntity(roadmapDto, image);

            return roadmapRepository.save(roadmap);
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

    @Override
    public void deleteById(String id) {
        try{
            Roadmap roadmap = roadmapCacheService.getById(id);
            deleteAllRelatedImageFromCloud(roadmap);
            roadmapRepository.delete(roadmap);
        } catch (NotFoundException ex){
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to delete roadmap : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to delete roadmap");
        }
    }

    private void deleteAllRelatedImageFromCloud(Roadmap roadmap) {
        List<String> allImage = commentService.getAllImagesFromComments(roadmap.getComments());

        // add roadmap image
        if(roadmap.getImage() != null && !roadmap.getImage().isEmpty())
            allImage.add(roadmap.getImage());

        // add commentsImages
        List<Comment> comments = roadmap.getComments();
        for(Comment comment : comments){
            if(comment.getImage() != null && !comment.getImage().isEmpty())
                allImage.add(comment.getImage());
            commentService.c
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

}
