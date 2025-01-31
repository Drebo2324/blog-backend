package com.drebo.blog.backend.services;

import com.drebo.blog.backend.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
}
