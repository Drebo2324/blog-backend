package com.drebo.blog.backend.controllers;

import com.drebo.blog.backend.domain.dtos.CategoryDto;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.services.implementations.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return List<Category> categories = categoryService.listCategories();
        //TODO: MAP TO DTO
    }
}
