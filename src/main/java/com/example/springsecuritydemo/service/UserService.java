package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    List<User> getUsers();

    User getUserById(UUID userId);

    User updateUser(UUID userId, User user);
}
