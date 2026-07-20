/* package com.generation..security;

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

    // Deve essere la STESSA chiave usata nel filtro JwtAuthenticationFilter!
    private final String SECRET_KEY = "la_tua_chiave_segreta_super_sicura_e_molto_lunga_per_brianzatrains";
    
    // Il token scadrà dopo 24 ore (in millisecondi)
    private final long EXPIRATION_TIME = 86400000; 

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Qui se vuoi puoi aggiungere altre informazioni nel token, es: claims.put("ruolo", "ADMIN");
        
        //  payload: mie credenziali autenticate, firmate
        //  sub: subject => utente, username (admin)

        claims.put("ROLE", user.getRole());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername()) // Il proprietario del token                       // sub
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))             // exp => tempo di scadenza
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
    */