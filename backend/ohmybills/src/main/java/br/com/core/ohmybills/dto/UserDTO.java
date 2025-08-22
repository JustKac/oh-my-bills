package br.com.core.ohmybills.dto;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        UUID keycloakId
) {}