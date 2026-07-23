package com.generation.techshare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.techshare.dto.LoginRequestDto;
import com.generation.techshare.dto.LoginResponseDto;
import com.generation.techshare.dto.UserDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.model.User;
import com.generation.techshare.security.JwtService;
import com.generation.techshare.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/techshare/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.findByEmail(loginRequestDto.getEmail());
        
        if (user != null && passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDto(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            // Il metodo insert di userService si occupa gia di cifrare la password in modo corretto
            UserDto saved = userService.insert(userDto);
            return ResponseEntity.ok(saved);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.toMap("Register"));
        }
    }
}