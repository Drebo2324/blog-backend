package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.CreatePostRequest;
import com.drebo.blog.backend.domain.PostStatus;
import com.drebo.blog.backend.domain.UpdatePostRequest;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.domain.entities.User;
import com.drebo.blog.backend.repositories.PostRepository;
import com.drebo.blog.backend.services.CategoryService;
import com.drebo.blog.backend.services.PostService;
import com.drebo.blog.backend.services.TagService;
import com.drebo.blog.backend.services.utils.ServiceUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new EntityNotFoundException("Post not found with ID:" + postId));
    }

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

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {

        Category category = categoryService.findCategoryById(createPostRequest.getCategoryId());
//        Set<Tag> tags = createPostRequest.getTagIds().stream().map(
//                tagService::findTagById).collect(Collectors.toSet());
        List<Tag> tags = tagService.findTagsById(createPostRequest.getTagIds());

        Post newPost = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .author(user)
                .postStatus(createPostRequest.getPostStatus())
                .category(category)
                .tags(new HashSet<>(tags))
                .readingTime(ServiceUtils.calculateReadingTime(createPostRequest.getContent()))
                .build();

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {

        Post existingPost = postRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Post does not exist with ID:" + id));

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setPostStatus(updatePostRequest.getPostStatus());
        existingPost.setReadingTime(ServiceUtils.calculateReadingTime(updatePostRequest.getContent()));

        //check category and tags change before making unnecessary call to db
        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category updateCategory = categoryService.findCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(updateCategory);
        }

        Set<UUID> existingTagsId = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> requestTagIds = updatePostRequest.getTagIds();
        if(!existingTagsId.equals(requestTagIds)) {
            List<Tag> updatedTagIds = tagService.findTagsById(requestTagIds);
            existingPost.setTags(new HashSet<>(updatedTagIds));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        //getPost will throw exception if not found
        Post post = getPost(id);
        postRepository.delete(post);
    }
}
