package br.com.core.ohmybills.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    // Redireciona para o fluxo OIDC do Keycloak
    @GetMapping("/auth/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/oauth2/authorization/keycloak")
                .build();
    }

    // Informações do usuário autenticado (suporta JWT bearer e sessão via oauth2Login)
    @GetMapping("/auth/me")
    public Map<String, Object> me(Authentication auth,
                                  @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                                  OAuth2AuthorizedClient client) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("authenticated", auth != null && auth.isAuthenticated());
        body.put("authorities", auth == null ? List.of() :
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            body.put("subject", jwt.getSubject());
            body.put("username", jwt.getClaimAsString("preferred_username"));
            body.put("email", jwt.getClaimAsString("email"));
            body.put("expiresAt", jwt.getExpiresAt());
        } else if (client != null && client.getAccessToken() != null) {
            assert auth != null;
            var principal = auth.getPrincipal();
            body.put("principal", principal.toString());
            body.put("expiresAt", client.getAccessToken().getExpiresAt());
        }
        return body;
    }

    // Exibe token atual e metadados (apenas para depuração; proteja este endpoint)
    @GetMapping("/auth/token")
    public Map<String, Object> token(Authentication auth,
                                     @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                                     OAuth2AuthorizedClient client) {
        Map<String, Object> body = new LinkedHashMap<>();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            body.put("accessToken", jwt.getTokenValue());
            body.put("expiresAt", jwt.getExpiresAt());
        } else if (client != null && client.getAccessToken() != null) {
            body.put("accessToken", client.getAccessToken().getTokenValue());
            body.put("expiresAt", client.getAccessToken().getExpiresAt());
            body.put("refreshTokenIssuedAt",
                    Optional.ofNullable(client.getRefreshToken()).map(AbstractOAuth2Token::getIssuedAt).orElse(null));
            body.put("refreshTokenExpiresAt",
                    Optional.ofNullable(client.getRefreshToken()).map(AbstractOAuth2Token::getExpiresAt).orElse(null));
        } else {
            body.put("error", "Nenhum token disponível");
        }
        return body;
    }

    // Logout centralizado (redireciona para o endpoint /logout que dispara o OIDC logout)
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/logout")
                .build();
    }
}