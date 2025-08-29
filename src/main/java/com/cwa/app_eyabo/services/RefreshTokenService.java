package com.cwa.app_eyabo.services;

import com.cwa.app_eyabo.dto.RefreshTokenDto;
import com.cwa.app_eyabo.entities.CustomUser;
import com.cwa.app_eyabo.entities.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(CustomUser customUser, RefreshTokenDto refreshTokenDto);
    RefreshToken verifyRefreshToken(String refreshToken );
    void deleteRefreshTokenByValue(String tokenValue);
    void deleteRefreshToken(RefreshToken refreshToken);
    void deleteByToken(String token);

    RefreshToken findByToken(String refreshTokenValue);
}
