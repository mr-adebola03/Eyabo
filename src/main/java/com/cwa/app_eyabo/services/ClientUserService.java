package com.cwa.app_eyabo.services;

import com.cwa.app_eyabo.dto.RegisterClientUserDto;
import com.cwa.app_eyabo.entities.Client;

public interface ClientUserService {
    Client registerClientUser(RegisterClientUserDto registerClientUserDto);
}
