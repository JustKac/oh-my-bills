package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.AuthTokenDTO;
import br.com.core.ohmybills.dto.AuthPrincipalDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public ResponseEntity<Void> buildLoginRedirect() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/oauth2/authorization/keycloak")
                .build();
    }

    @Override
    public ResponseEntity<Void> buildLogoutRedirect() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/logout")
                .build();
    }

    @Override
    public AuthTokenDTO getCurrentToken(Authentication auth, OAuth2AuthorizedClient client) {
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            return new AuthTokenDTO(
                    jwt.getTokenValue(),
                    jwt.getExpiresAt(),
                    null,
                    null
            );
        }

        if (client != null && client.getAccessToken() != null) {
            String accessToken = client.getAccessToken().getTokenValue();
            Instant accessTokenExpiresAt = client.getAccessToken().getExpiresAt();
            Instant refreshIssuedAt = client.getRefreshToken() != null ? client.getRefreshToken().getIssuedAt() : null;
            Instant refreshExpiresAt = client.getRefreshToken() != null ? client.getRefreshToken().getExpiresAt() : null;
            return new AuthTokenDTO(accessToken, accessTokenExpiresAt, refreshIssuedAt, refreshExpiresAt);
        }

        throw new IllegalStateException("Nenhum token disponível para o contexto atual.");
    }

    @Override
    public AuthPrincipalDTO resolvePrincipal(Authentication auth, OAuth2AuthorizedClient client) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Requisição não autenticada.");
        }

        String sub;
        String email;
        String name;

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            sub = jwt.getSubject();
            email = jwt.getClaimAsString("email");
            name = firstNonBlank(jwt.getClaimAsString("name"), jwt.getClaimAsString("preferred_username"));
        } else if (auth.getPrincipal() instanceof OidcUser oidcUser) {
            Map<String, Object> claims = oidcUser.getClaims();
            sub = oidcUser.getSubject();
            email = (String) claims.get("email");
            name = firstNonBlank((String) claims.get("name"), (String) claims.get("preferred_username"));
        } else {
            throw new IllegalStateException("Tipo de autenticação não suportado para resolver Keycloak ID.");
        }

        if (sub == null || sub.isBlank()) {
            throw new IllegalStateException("Keycloak 'sub' ausente no token.");
        }

        return new AuthPrincipalDTO(parseUuid(sub), email, name);
    }

    private String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    private UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("O 'sub' do Keycloak não é um UUID válido: " + value);
        }
    }
}