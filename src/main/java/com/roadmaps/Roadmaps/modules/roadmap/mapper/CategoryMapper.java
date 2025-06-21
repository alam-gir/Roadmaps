package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.CategoryRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.response.CategoryResponseDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public CategoryResponseDto toResponseDto (Category category) {
        return new CategoryResponseDto(
                category.getId().toString(),
                category.getName()
        );
    }

    public List<CategoryResponseDto> toResponseDtoList(List<Category> categories) {
        return categories.stream().map(this::toResponseDto).toList();
    }
}
