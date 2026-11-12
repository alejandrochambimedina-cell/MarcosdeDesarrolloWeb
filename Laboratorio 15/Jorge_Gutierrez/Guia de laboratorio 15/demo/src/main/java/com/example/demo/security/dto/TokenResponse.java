package com.example.demo.security.dto;

public record TokenResponse(
        String access_token,
        String token_type,
        long expires_in) {
}
