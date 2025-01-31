package com.drebo.blog.backend.mappers;

import com.drebo.blog.backend.domain.PostStatus;
import com.drebo.blog.backend.domain.dtos.TagDto;
import com.drebo.blog.backend.domain.entities.Post;
import com.drebo.blog.backend.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toDto(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {

        if(posts == null) {
            return 0;
        }

        return (int) posts.stream()
                .filter(post ->
                        PostStatus.PUBLISHED.equals(post.getPostStatus()))
                .count();
    }
}
