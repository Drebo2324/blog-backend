package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.repositories.CategoryRepository;
import com.drebo.blog.backend.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

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

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);

        //CAN ONLY DELETE CATEGORIES WITH NO POSTS
        if(category.isPresent()) {
            if(!category.get().getPosts().isEmpty()){
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

    //Not returning Optional. Throw Exception
    @Override
    public Category FindCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Category not found with ID:" + id)
        );
    }
}
