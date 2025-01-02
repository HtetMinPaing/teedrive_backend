package com.example.teedrive.domain.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }
}