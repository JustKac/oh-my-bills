package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Redireciona para o fluxo OIDC do Keycloak
    @GetMapping("/auth/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, "/oauth2/authorization/keycloak")
                .build();
    }

    // Retorna (ou cria) o usuário vinculado ao Keycloak ID
    @GetMapping("/auth/me")
    public UserDTO me(Authentication auth,
                      @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                      OAuth2AuthorizedClient client) {
        return userService.getMe(auth, client);
    }

    // Exibe token atual e metadados (apenas para depuração; proteja este endpoint)
    @GetMapping("/auth/token")
    public java.util.Map<String, Object> token(Authentication auth,
                                               @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                                               OAuth2AuthorizedClient client) {
        java.util.Map<String, Object> body = new java.util.LinkedHashMap<>();
        if (auth instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            body.put("accessToken", jwt.getTokenValue());
            body.put("expiresAt", jwt.getExpiresAt());
        } else if (client != null && client.getAccessToken() != null) {
            body.put("accessToken", client.getAccessToken().getTokenValue());
            body.put("expiresAt", client.getAccessToken().getExpiresAt());
            body.put("refreshTokenIssuedAt",
                    java.util.Optional.ofNullable(client.getRefreshToken()).map(org.springframework.security.oauth2.core.AbstractOAuth2Token::getIssuedAt).orElse(null));
            body.put("refreshTokenExpiresAt",
                    java.util.Optional.ofNullable(client.getRefreshToken()).map(org.springframework.security.oauth2.core.AbstractOAuth2Token::getExpiresAt).orElse(null));
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