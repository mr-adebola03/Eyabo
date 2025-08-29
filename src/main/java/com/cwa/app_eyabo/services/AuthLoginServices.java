package com.cwa.app_eyabo.services;

import com.cwa.app_eyabo.dto.LoginAuthRequestDto;
import com.cwa.app_eyabo.entities.CustomUser;
import com.cwa.app_eyabo.repositories.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginServices {

    private final AuthenticationManager authenticationManager;
    private final CustomUserRepository customUserRepository;

    public CustomUser authenticate(LoginAuthRequestDto loginAuthRequestDto, String ipAddress, String userAgent) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginAuthRequestDto.getEmail(), loginAuthRequestDto.getPassword())
        );
        return customUserRepository.findByEmail(loginAuthRequestDto.getEmail())
                .orElseThrow();
    }
}
