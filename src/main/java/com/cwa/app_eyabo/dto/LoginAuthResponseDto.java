package com.cwa.app_eyabo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAuthResponseDto {
    private String tokenType = "Bearer";
    private long expiresIn;
    private String accessToken;
    private String refreshToken;

    public LoginAuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
