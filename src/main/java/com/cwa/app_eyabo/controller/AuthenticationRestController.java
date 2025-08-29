package com.cwa.app_eyabo.controller;

import com.cwa.app_eyabo.entities.Client;
import com.cwa.app_eyabo.services.AuthLoginServices;
import com.cwa.app_eyabo.config.JwtUtils;
import com.cwa.app_eyabo.dto.*;
import com.cwa.app_eyabo.entities.CustomUser;
import com.cwa.app_eyabo.entities.RefreshToken;
import com.cwa.app_eyabo.repositories.CustomUserRepository;
import com.cwa.app_eyabo.services.ClientUserService;
import com.cwa.app_eyabo.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    private final CustomUserRepository customUserRepository;
    private final ClientUserService clientUserService;
    private final AuthLoginServices authLoginServices;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    AuthenticationRestController(
            CustomUserRepository customUserRepository,
            ClientUserService clientUserService,
            AuthLoginServices authLoginServices,
            JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.customUserRepository = customUserRepository;
        this.clientUserService = clientUserService;
        this.authLoginServices = authLoginServices;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Client> registerClientUser(@RequestBody RegisterClientUserDto registerClientUserDto) {
        Client client = clientUserService.registerClientUser(registerClientUserDto);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginAuthResponseDto> authenticate(@RequestBody LoginAuthRequestDto loginAuthRequestDto,
                                                             HttpServletRequest request, HttpServletResponse response) {


        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        CustomUser authenticatedUser = authLoginServices.authenticate(loginAuthRequestDto, ipAddress, userAgent);

        String jwtToken = jwtUtils.generateToken((UserDetails) authenticatedUser);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", jwtToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(900000))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setIpAddress(ipAddress);
        refreshTokenDto.setUserAgent(userAgent);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser, refreshTokenDto);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshTokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        LoginAuthResponseDto loginAuthResponseDto = new LoginAuthResponseDto();
        loginAuthResponseDto.setAccessToken(jwtToken);
        loginAuthResponseDto.setExpiresIn(jwtUtils.getExpirationTime());
        loginAuthResponseDto.setRefreshToken(refreshToken.getRefreshToken());

        return ResponseEntity.ok(loginAuthResponseDto);

    }
}