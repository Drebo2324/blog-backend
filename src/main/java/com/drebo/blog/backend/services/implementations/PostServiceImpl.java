package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.PostStatus;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.repositories.PostRepository;
import com.drebo.blog.backend.services.CategoryService;
import com.drebo.blog.backend.services.PostService;
import com.drebo.blog.backend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Transactional(readOnly = true) //no db calls make changes
    @Override
    //method can handle null params
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            Tag tag = tagService.findTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag);
        }

        if(categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category);
        }

        if (tagId != null) {
            Tag tag = tagService.findTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }
}
