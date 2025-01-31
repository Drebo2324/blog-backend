package com.drebo.blog.backend.services.implementations;

import com.drebo.blog.backend.domain.entities.User;
import com.drebo.blog.backend.repositories.UserRepository;
import com.drebo.blog.backend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User not found with ID:" + userId));
    }
}
