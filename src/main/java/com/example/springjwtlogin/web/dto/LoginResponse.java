package com.example.springjwtlogin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private final String jwt;
    private final String username;
}
