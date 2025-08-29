package com.cwa.app_eyabo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    private UUID id;
    private String refreshToken;
    private Instant expiryDate;
    private boolean revoked;
    private String ipAddress;
    private String userAgent;
    private Date created_at;
}
