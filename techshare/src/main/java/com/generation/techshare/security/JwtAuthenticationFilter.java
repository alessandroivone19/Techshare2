package com.generation.techshare.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SECRET_KEY = "la_tua_chiave_segreta_super_sicura_e_molto_lunga_per_brianzatrains";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 1. Ignora sempre le richieste HTTP OPTIONS (necessarie per il CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 2. Ignora la verifica del token per i path pubblici
        if ("GET".equalsIgnoreCase(method) && path.startsWith("/techshare/api/users/public/")) {
            return true;
        }
        if (path.startsWith("/techshare/api/auth/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                var claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String username = claims.getSubject();
                String role = claims.get("ROLE", String.class);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Usa java.util.Collections al posto del package interno di JJWT
                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                    
                    if (role != null) {
                        // Se il ruolo ha già il prefisso "ROLE_", evita di raddoppiarlo
                        String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                        authorities = List.of(new SimpleGrantedAuthority(formattedRole));
                    }

                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // In caso di token invalido/scaduto, svuota il contesto di sicurezza
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}