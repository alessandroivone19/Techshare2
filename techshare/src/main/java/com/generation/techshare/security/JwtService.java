package com.generation.techshare.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.generation.techshare.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "la_tua_chiave_segreta_super_sicura_e_molto_lunga_per_brianzatrains";
    private final long EXPIRATION_TIME = 86400000; 

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ROLE", user.getRole());
        claims.put("userId", user.getId());

        // 👈 AGGIUNTI NOME E COGNOME NEI CLAIMS DEL JWT
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName()); // Se nella tua entity User si chiama getSurname(), usa getSurname()

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}