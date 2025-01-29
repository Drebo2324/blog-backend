package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<Tag> listTags();
    List<Tag> createTags(Set<String> tagNames);
}
