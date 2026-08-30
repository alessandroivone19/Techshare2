package com.generation.techshare.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.generation.techshare.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "la_tua_chiave_segreta_super_sicura_e_molto_lunga_per_brianzatrains";
    private final long EXPIRATION_TIME = 86400000; 

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("ROLE", user.getRole())
                .claim("userId", user.getId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}