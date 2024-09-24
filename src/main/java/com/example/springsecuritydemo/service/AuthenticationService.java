package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.model.AuthenticationRequest;
import com.example.springsecuritydemo.model.AuthenticationResponse;
import com.example.springsecuritydemo.model.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);
    AuthenticationResponse login(AuthenticationRequest request);
}
