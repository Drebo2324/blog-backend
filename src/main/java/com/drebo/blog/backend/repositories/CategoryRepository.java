package com.drebo.blog.backend.repositories;

import com.drebo.blog.backend.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Hibernate Query Language (HQL)
    // Retrieve all Category entities from the database.
    // Use LEFT JOIN FETCH to ensure all categories are included,
    // even if they do not have any associated posts.
    // Fetch the posts along with each category in a single query
    // to avoid the N+1 problem (multiple queries for posts).
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    boolean existsByNameIgnoreCase(String name);
}
