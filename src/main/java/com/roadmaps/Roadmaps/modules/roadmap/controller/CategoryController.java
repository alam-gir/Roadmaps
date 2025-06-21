package com.roadmaps.Roadmaps.modules.roadmap.controller;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.modules.roadmap.dtos.CategoryRequestDto;
import com.roadmaps.Roadmaps.modules.roadmap.entity.Category;
import com.roadmaps.Roadmaps.modules.roadmap.mapper.CategoryMapper;
import com.roadmaps.Roadmaps.modules.roadmap.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    ResponseEntity<?> getCategories(){
        List<Category> categories = categoryService.getAll();

        ApiResponse<?> apiResponse = ApiResponse.success(
                categoryMapper.toResponseDtoList(categories),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getCategoryById(@PathVariable String id){
        Category category = categoryService.getById(id);

        ApiResponse<?> apiResponse = ApiResponse.success(
                categoryMapper.toResponseDto(category),
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@Valid @ModelAttribute CategoryRequestDto dto) {
        categoryService.add(dto);

        ApiResponse<?> apiResponse = ApiResponse.success(null, "Category added.");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @Valid @ModelAttribute CategoryRequestDto dto) {
        Category category = categoryService.update(id, dto);

        ApiResponse<?> apiResponse = ApiResponse.success(categoryMapper.toResponseDto(category), "Category updated.");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
