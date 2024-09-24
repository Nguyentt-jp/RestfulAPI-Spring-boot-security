package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.dto.UserDTO;
import com.example.springsecuritydemo.entity.Role;
import com.example.springsecuritydemo.entity.Token;
import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.model.AuthenticationRequest;
import com.example.springsecuritydemo.model.AuthenticationResponse;
import com.example.springsecuritydemo.model.RegisterRequest;
import com.example.springsecuritydemo.repository.TokenRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.AuthenticationService;
import com.example.springsecuritydemo.service.JwtService;
import lombok.RequiredArgsConstructor;
import mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User newUser = new User();
        newUser.setUsername(registerRequest.getUserName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRole(Role.ADMIN);
        User createdUser = userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(createdUser);

        Token token = Token.builder()
                .userId(createdUser.getId())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .userDTO(UserMapper.mapToUserDto(createdUser))
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        UserDTO userDTO = UserMapper.mapToUserDto(user);
        return AuthenticationResponse.builder()
                .userDTO(userDTO)
                .token(jwtToken)
                .build();
    }
}
