package com.cwa.app_eyabo.repositories;

import com.cwa.app_eyabo.entities.CustomUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface CustomUserRepository extends JpaRepository<CustomUser, UUID> {
    Optional<CustomUser> findByEmail(@Param("email") String email);
    Boolean existsByEmail(String email);
}
