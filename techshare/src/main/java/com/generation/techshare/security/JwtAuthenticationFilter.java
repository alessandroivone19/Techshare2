package com.generation.techshare.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // NOTA: In produzione questa chiave deve essere lunga almeno 256 bit e stare in application.properties
    private final String SECRET_KEY = "la_tua_chiave_segreta_super_sicura_e_molto_lunga_per_brianzatrains";

    @Override
    protected void doFilterInternal
    (
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    )
    throws ServletException, IOException {
        
        // recupero il valore dell'header Authorization, che è quello che ho impostato
        // nella chiamata http
        String authHeader = request.getHeader("Authorization");
        //'Bearer '+token

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Rimuove "Bearer " ed estrae solo il token
            
            try {
                // Recuperiamo l'intero payload (claims) del token con una sola operazione di parsing
                var claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String username = claims.getSubject();
                
                // Estraiamo il ruolo dal claim personalizzato "role" che abbiamo impostato nel JwtService
                String role = claims.get("ROLE", String.class);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    // Creiamo la lista di autorità. Se il ruolo esiste, gli iniettiamo davanti il prefisso "ROLE_"
                    // In questo modo .hasRole("ADMIN") cercherà esattamente "ROLE_ADMIN" e darà il via libera.
                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                    if (role != null) {
                        authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    }

                    // Passiamo le autorità reali al token di autenticazione anziché la lista vuota
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    
                    // Salva l'utente nel contesto con i suoi ruoli attivi
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Token scaduto, manomesso o non valido: non facciamo nulla, la richiesta fallirà l'autorizzazione
                e.printStackTrace();
            }
        }

        // Passa la richiesta al filtro successivo della catena
        filterChain.doFilter(request, response);
    }
}