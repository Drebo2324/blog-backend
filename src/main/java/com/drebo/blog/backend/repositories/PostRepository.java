package com.drebo.blog.backend.repositories;

import com.drebo.blog.backend.domain.PostStatus;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    //Jpa magic
    List<Post> findAllByStatusAndCategoryAndTagsContaining(
            PostStatus postStatus,
            Category category,
            Tag tag);

    List<Post> findAllByStatusAndCategory(PostStatus postStatus, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus postStatus, Tag tag);
    List<Post> findAllByStatus(PostStatus postStatus);

}
