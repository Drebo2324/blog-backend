package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.repositories.TagRepository;
import com.drebo.blog.backend.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Transactional
    @Override
    public void deleteTag(UUID id) {
        //cannot delete tag if it has existing posts
        tagRepository.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with a post");
            }
            tagRepository.deleteById(id);
        });
    }

    //throw exception rather than return optional
    @Override
    public Tag findTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Tag not found with ID:" + id));
    }
}
