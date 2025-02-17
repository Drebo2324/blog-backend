package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
    void deleteCategory(UUID id);
    Category findCategoryById(UUID id);
    Category updateCategory(UUID id, Category category);
}
