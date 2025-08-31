package br.com.core.ohmybills.dto;

import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record TagDTO(
        UUID tagId,
        String name,
        Boolean isPerson,
        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid hex code")
        String color) {
}
