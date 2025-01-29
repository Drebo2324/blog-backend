package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.repositories.TagRepository;
import com.drebo.blog.backend.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional //all calls to db happen in same transaction
    @Override
    public List<Tag> createTags(Set<String> tagNames) {

        //check if tags already exist
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        //collect existing tag names for comparison
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        //create new Tag for all non-existing tag name
        List<Tag> newTags = tagNames.stream().filter(name ->
                        !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }
}
