package com.roadmaps.Roadmaps.modules.roadmap.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Upvote;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.RoadmapMapper;
import com.roadmaps.Roadmaps.modules.roadmap.service.RoadmapService;
import com.roadmaps.Roadmaps.modules.roadmap.service.UpvoteService;
import com.roadmaps.Roadmaps.security.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/roadmaps")
@RequiredArgsConstructor
@Validated
public class RoadmapController {
    private final RoadmapService roadmapService;
    private final RoadmapMapper roadmapMapper;
    private final UpvoteService upvoteService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<?>> getRoadmapById(@PathVariable String id) {

        Roadmap roadmap = roadmapService.getById(id);

        ApiResponse<?> apiResponse = ApiResponse.success(
                roadmapMapper.toResponseDto(roadmap),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addRoadmap(RoadmapRequestDto roadmapDto) {

        roadmapService.addRoadmap(roadmapDto);

        ApiResponse<?> apiResponse = ApiResponse.success(
                null,
                "Roadmap Added."
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/upvote/roadmap/{roadmapId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<?>> upvoteToRoadmap(Authentication authentication, @PathVariable String roadmapId) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        List<Upvote> upvotes = upvoteService.upvoteByUserEmailAndRoadmapId(user.getEmail(), roadmapId);

        ApiResponse<?> apiResponse = ApiResponse.success(
                roadmapMapper.toUpvoteResponseDtoList(upvotes),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/upvote/comment/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<?>> upvoteToComment(Authentication authentication, @PathVariable String commentId) {
        UserPrinciple user =  (UserPrinciple) authentication.getPrincipal();

        List<Upvote> upvotes = upvoteService.upvoteByUserEmailAndCommentId(user.getEmail(), commentId);

        ApiResponse<?> apiResponse = ApiResponse.success(
                roadmapMapper.toUpvoteResponseDtoList(upvotes),
                null
        );

        return ResponseEntity.ok(apiResponse);
    }
}
