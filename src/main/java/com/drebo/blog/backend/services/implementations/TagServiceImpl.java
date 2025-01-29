package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.Tag;
import com.drebo.blog.backend.repositories.TagRepository;
import com.drebo.blog.backend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAllWithPostCount();
    }
}
