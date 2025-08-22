package br.com.core.ohmybills.dto;

import java.time.Instant;

public record AuthTokenDTO(
        String accessToken,
        Instant expiresAt,
        Instant refreshTokenIssuedAt,
        Instant refreshTokenExpiresAt
) {}