package com.drebo.blog.backend.controllers;

import com.drebo.blog.backend.domain.dtos.TagDto;
import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.mappers.TagMapper;
import com.drebo.blog.backend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {

        List<Tag> tags = tagService.listTags();
        List<TagDto> tagDtoList = tags.stream().map(tagMapper::toDto).toList();

        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
    }
}
