package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
