package com.cwa.app_eyabo.services;

import com.cwa.app_eyabo.dto.RegisterClientUserDto;
import com.cwa.app_eyabo.entities.Client;
import com.cwa.app_eyabo.repositories.CustomUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientUserServiceImplement implements ClientUserService {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;

    ClientUserServiceImplement(
            final CustomUserRepository customUserRepository,
            final PasswordEncoder passwordEncoder
    ){
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client registerClientUser(RegisterClientUserDto registerClientUserDto) {
        if (customUserRepository.existsByEmail(registerClientUserDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (registerClientUserDto.getNomEntreprise() == null) {
            Client client = new Client();

            client.setNomClient(registerClientUserDto.getNomClient());
            client.setPrenomClient(registerClientUserDto.getPrenomClient());
            client.setEmail(registerClientUserDto.getEmail());
            client.setPassword(passwordEncoder.encode(registerClientUserDto.getPassword()));
            this.customUserRepository.save(client);
            return client;
        }
        return null;
    }
}
