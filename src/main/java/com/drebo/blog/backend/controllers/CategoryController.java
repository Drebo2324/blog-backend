package com.drebo.blog.backend.controllers;

import com.drebo.blog.backend.domain.dtos.CategoryDto;
import com.drebo.blog.backend.domain.dtos.CreateCategoryRequest;
import com.drebo.blog.backend.domain.dtos.UpdateCategoryRequest;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.mappers.CategoryMapper;
import com.drebo.blog.backend.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
        List<CategoryDto> categories = categoryService.listCategories()
                .stream().map(categoryMapper::toDto).toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest request){
        Category entity = categoryMapper.toEntity(request);
        Category createdCategory = categoryService.createCategory(entity);
        return new ResponseEntity<>(
                categoryMapper.toDto(createdCategory),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryRequest request) {

        Category category = categoryMapper.toEntity(request);
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryDto categoryDto = categoryMapper.toDto(updatedCategory);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
