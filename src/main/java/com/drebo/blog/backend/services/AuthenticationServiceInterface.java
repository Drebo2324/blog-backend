package com.drebo.blog.backend.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationServiceInterface {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
}
