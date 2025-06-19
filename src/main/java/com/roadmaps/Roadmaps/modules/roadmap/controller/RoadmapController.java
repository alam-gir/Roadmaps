package com.roadmaps.Roadmaps.modules.roadmap.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.RoadmapRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/roadmaps")
@RequiredArgsConstructor
@Validated
public class RoadmapController {
    private final RoadmapService roadmapService;

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
