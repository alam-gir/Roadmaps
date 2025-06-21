package com.roadmaps.Roadmaps.modules.roadmap.service;

import com.roadmaps.Roadmaps.modules.roadmap.dtos.CategoryRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(String id);

    Category add(CategoryRequestDto categoryDto);

    Category update(String id, CategoryRequestDto dto);

    void delete(String id);
}
