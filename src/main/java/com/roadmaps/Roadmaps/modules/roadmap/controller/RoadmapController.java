package com.roadmaps.Roadmaps.modules.roadmap.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Roadmap;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.RoadmapMapper;
import com.roadmaps.Roadmaps.modules.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/roadmaps")
@RequiredArgsConstructor
@Validated
public class RoadmapController {
    private final RoadmapService roadmapService;
    private final RoadmapMapper roadmapMapper;

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
}
