package com.generation.techshare.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    String token;

    // costruttore con token
    public LoginResponseDto(String token) {
        this.token = token;
    }
}
