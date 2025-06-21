package com.roadmaps.Roadmaps.modules.roadmap.service.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.exceptions.NotFoundException;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.CategoryRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.CategoryMapper;
import com.roadmaps.Roadmaps.modules.roadmap.repository.CategoryRepository;
import com.roadmaps.Roadmaps.modules.roadmap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getAll() {
        try{
            return categoryRepository.findAll();
        } catch (Exception ex){
            log.error("Categories Not Found : {}", ex.getMessage(), ex);
            throw new ApiException("Categoris Not Found");
        }
    }

    @Override
    public Category getById(String id) {
        try{
            UUID uuid = UUID.fromString(id);
            return categoryRepository.findById(uuid)
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));
        } catch (NotFoundException ex){
            log.warn("Category Not Found : {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex){
            log.error("Category Not Found : {}", ex.getMessage(), ex);
            throw new ApiException("Category Not Found");
        }
    }

    @Override
    public Category add(CategoryRequestDto categoryDto) {
        try {
            validateCategoryNotExist(categoryDto.getName());
            Category newCategory = categoryMapper.toEntity(categoryDto);
            return categoryRepository.save(newCategory);
        } catch (ApiException ex) {
            log.error("Failed to add new category : {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to add new category : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to add new category");
        }
    }

    @Override
    public void delete(String id) {
        try{
            Category category = getById(id);
            categoryRepository.delete(category);
        } catch (ApiException ex) {
            log.error("Failed to delete category : {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to delete category : {}", ex.getMessage(), ex);
            throw new ApiException("Failed to delete category");
        }
    }

    private void validateCategoryNotExist(String name) {
        categoryRepository.findByNameIgnoreCase(name)
                .ifPresent(c -> {
                    throw  new ApiException("Category already exists");
                });
    }
}
