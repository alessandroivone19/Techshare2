package com.generation.techshare.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Abilita @PreAuthorize e @PostAuthorize
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoint pubblici (no autenticazione)
                .requestMatchers("/techshare/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/techshare/api/categories").permitAll()        // GET categories per tutti
                .requestMatchers(HttpMethod.GET, "/techshare/api/categories/**").permitAll()     // GET categoria singola per tutti
                .requestMatchers(HttpMethod.GET, "/techshare/api/equipments").permitAll()        // GET equipments per tutti
                .requestMatchers(HttpMethod.GET, "/techshare/api/equipments/**").permitAll()     // GET equipment singolo per tutti
                .requestMatchers(HttpMethod.POST,"/techshare/api/users").permitAll()           // POST requests per tutti
                
                // Tutti gli altri endpoint richiedono autenticazione
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}