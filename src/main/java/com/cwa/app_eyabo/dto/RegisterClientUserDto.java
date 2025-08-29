package com.cwa.app_eyabo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientUserDto {
    private String nomClient;
    private String prenomClient;
    private String adresseClient;
    private String nomEntreprise;
    private String adresseEntreprise;
    private String email;
    private String password;
}
