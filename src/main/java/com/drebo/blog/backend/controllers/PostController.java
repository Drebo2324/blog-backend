package com.drebo.blog.backend.controllers;

import com.drebo.blog.backend.domain.CreatePostRequest;
import com.drebo.blog.backend.domain.UpdatePostRequest;
import com.drebo.blog.backend.domain.dtos.CreatePostRequestDto;
import com.drebo.blog.backend.domain.dtos.PostDto;
import com.drebo.blog.backend.domain.dtos.UpdatePostRequestDto;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.User;
import com.drebo.blog.backend.mappers.PostMapper;
import com.drebo.blog.backend.services.PostService;
import com.drebo.blog.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {

        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postsDto = posts.stream().map(postMapper::toDto).toList();
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    //filter drafts based on logged-in user with userId
    //TODO: Implement proper Authorization
    @GetMapping(path = "/drafts")
    public  ResponseEntity<List<PostDto>> getAllDrafts(@RequestAttribute UUID userId) {

        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);

        List<PostDto> draftPostsDto = draftPosts.stream().map(postMapper::toDto).toList();

        return new ResponseEntity<>(draftPostsDto, HttpStatus.OK);
    }

    //use userId so only logged-in user can create posts
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequestDto request,
                                                 @RequestAttribute UUID userId) {

        User loggedInUser = userService.getUserById(userId);

        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(request);

        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto request) {

        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(request);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto postDto = postMapper.toDto(updatedPost);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
