package com.drebo.blog.backend.mappers;

import com.drebo.blog.backend.domain.CreatePostRequest;
import com.drebo.blog.backend.domain.UpdatePostRequest;
import com.drebo.blog.backend.domain.dtos.CreatePostRequestDto;
import com.drebo.blog.backend.domain.dtos.PostDto;
import com.drebo.blog.backend.domain.dtos.UpdatePostRequestDto;
import com.drebo.blog.backend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "postStatus", source = "postStatus")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);

}
