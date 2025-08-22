package br.com.core.ohmybills.dto;

import java.util.UUID;

public record AuthPrincipalDTO(
        UUID keycloakId,
        String email,
        String name
) {}