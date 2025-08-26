package br.com.core.ohmybills.security;

import java.util.UUID;

public record UserContext(UUID userId, UUID keycloakId) {}