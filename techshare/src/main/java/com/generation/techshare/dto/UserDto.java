package com.generation.techshare.dto;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;
    private String firstName;
    private String password;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String city;
    private String role; 

}