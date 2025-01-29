package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listTags();
}
