package br.com.core.ohmybills.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String resourceClientId;

    public KeycloakAuthoritiesConverter(String resourceClientId) {
        this.resourceClientId = resourceClientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<String> realmRoles = Optional.ofNullable((Map<String, Object>) jwt.getClaim("realm_access"))
                .map(m -> (Collection<String>) m.getOrDefault("roles", Collections.emptyList()))
                .orElse(Collections.emptyList());

        Collection<String> resourceRoles = Optional.ofNullable((Map<String, Object>) jwt.getClaim("resource_access"))
                .map(m -> (Map<String, Object>) m.get(resourceClientId))
                .map(m -> (Collection<String>) m.getOrDefault("roles", Collections.emptyList()))
                .orElse(Collections.emptyList());

        return Stream.concat(realmRoles.stream(), resourceRoles.stream())
                .distinct()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase(Locale.ROOT))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}