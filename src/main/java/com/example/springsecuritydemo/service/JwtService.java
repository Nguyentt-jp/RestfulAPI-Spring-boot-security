package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.entity.User;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
}
