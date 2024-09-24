package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "vwhAw+2n6+Oty0cnaE4UjZSnuvTEI9zUqU4T+EXp32ZEozb7KygbbKjpo0+TK+O9H8D5KCoMLMkEYEnS604TWAkIjOggndADx0+3lLPHYQnsqen3AzE8BncoLIRSmUHff5bMNqEBHdQeY4GFIfYZFO9aXPz3jhp4QV4z7Nsj095CRkytyxU0tIv6359kmg4FLt5JG5lcEyKU0rcU1kEYkSqfUjVngvrbogoShVWIMxtIdkdKeiu9v/VAe5hd+S4FIrG+wEsYqJtnVxnx7mzOyqE7llgUrZswyZ7WVVBr0btJLEZuMkZZSbDubRxO8gU3OzjyYVjbdsw6AgWTDqPh";

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(Date.from(Instant.now()));
            if (!isExpired) {
                return claims.getSubject();
            }else {
                return null;
            }
        }
        return null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token )
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
