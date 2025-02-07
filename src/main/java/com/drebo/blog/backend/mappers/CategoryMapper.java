package com.drebo.blog.backend.mappers;

import com.drebo.blog.backend.domain.PostStatus;
import com.drebo.blog.backend.domain.dtos.CategoryDto;
import com.drebo.blog.backend.domain.dtos.CreateCategoryRequest;
import com.drebo.blog.backend.domain.dtos.UpdateCategoryRequest;
import com.drebo.blog.backend.domain.entities.Category;
import com.drebo.blog.backend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

//MAPSTRUCT WILL IMPLEMENT INTERFACE

//ignore properties that can't be mapped (not throw exception)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    //populate value of postCount of dto from posts
    //Use calculatePostCount method to do it
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);
    Category toEntity(UpdateCategoryRequest updateCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (null == posts){
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getPostStatus()))
                .count();
    }


}
