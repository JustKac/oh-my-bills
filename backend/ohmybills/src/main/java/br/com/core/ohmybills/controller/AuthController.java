package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.AuthTokenDTO;
import br.com.core.ohmybills.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth/login")
    public ResponseEntity<Void> login() {
        return authService.buildLoginRedirect();
    }

    @GetMapping("/auth/token")
    public AuthTokenDTO token(Authentication auth,
                              @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                              OAuth2AuthorizedClient client) {
        return authService.getCurrentToken(auth, client);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        return authService.buildLogoutRedirect();
    }
}