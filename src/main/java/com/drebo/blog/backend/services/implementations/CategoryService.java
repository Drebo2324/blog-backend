package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.repositories.CategoryRepository;
import com.drebo.blog.backend.services.CategoryServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    //all db calls completed  or fail
    @Transactional
    public Category createCategory(Category category){
        String categoryName = category.getName();
        if (categoryRepository.existsByNameIgnoreCase(categoryName)){
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }
        return categoryRepository.save(category);
    }
}
