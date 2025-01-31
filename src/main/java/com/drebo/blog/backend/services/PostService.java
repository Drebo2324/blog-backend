package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
}
