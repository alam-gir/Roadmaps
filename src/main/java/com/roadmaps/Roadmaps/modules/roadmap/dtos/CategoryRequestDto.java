package com.roadmaps.Roadmaps.modules.roadmap.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull(message = "Category name required!") @NotBlank(message = "Category name required!")
    @Size(max = 30, min = 3, message = "Category name must be in between 3 to 30 characters long.")
    private String name;
}
