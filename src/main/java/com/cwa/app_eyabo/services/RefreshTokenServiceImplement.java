package com.cwa.app_eyabo.services;

import com.cwa.app_eyabo.dto.RefreshTokenDto;
import com.cwa.app_eyabo.entities.CustomUser;
import com.cwa.app_eyabo.entities.RefreshToken;
import com.cwa.app_eyabo.repositories.RefreshTokenRepository;
import com.cwa.app_eyabo.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImplement implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    RefreshTokenServiceImplement(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(CustomUser customUser, RefreshTokenDto refreshTokenDto) {

        byte[] randomBytes = new byte[64]; // 512 bits
        new SecureRandom().nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
//        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setCustomUser(customUser);
        refreshToken.setIpAddress(refreshTokenDto.getIpAddress());
        refreshToken.setUserAgent(refreshTokenDto.getUserAgent());
        refreshToken.setCreated_at(Date.from(Instant.now()));
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken Token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token non trouvé"));

        if (Token.isRevoked()) {
            throw new RuntimeException("Refresh Token révoqué");
        }

        if (Token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh Token expiré");
        }

        return Token;
    }

    public void deleteRefreshTokenByValue(String tokenValue) {
        refreshTokenRepository.deleteByRefreshToken(tokenValue);
    }

    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.findByRefreshToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public RefreshToken findByToken(String refreshTokenValue) {
        return refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

}
