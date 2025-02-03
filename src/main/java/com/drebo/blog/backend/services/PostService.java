package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.CreatePostRequest;
import com.drebo.blog.backend.domain.UpdatePostRequest;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Post getPost(UUID postId);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
}
