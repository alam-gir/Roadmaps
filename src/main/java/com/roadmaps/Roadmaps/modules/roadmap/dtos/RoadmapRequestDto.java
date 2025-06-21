package com.roadmaps.Roadmaps.modules.roadmap.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoadmapRequestDto {
    private String text;

    private MultipartFile image;

    @NotEmpty(message = "Category id required!") @NotEmpty( message = "Category id required!")
    private String categoryId;

    @NotEmpty(message = "Status required!") @NotBlank(message = "Status Required!")
    private String status;
}
