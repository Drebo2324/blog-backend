package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.Category;

import java.util.List;

public interface CategoryServiceInterface {
    List<Category> listCategories();
    Category createCategory(Category category);
}
