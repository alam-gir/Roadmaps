package com.roadmaps.Roadmaps.modules.roadmap.mapper;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.CategoryRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
