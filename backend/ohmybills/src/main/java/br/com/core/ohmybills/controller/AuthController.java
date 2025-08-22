package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.AuthTokenDTO;
import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.service.AuthService;
import br.com.core.ohmybills.service.UserService;
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
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/auth/login")
    public ResponseEntity<Void> login() {
        return authService.buildLoginRedirect();
    }

    @GetMapping("/auth/me")
    public UserDTO me(Authentication auth,
                      @RegisteredOAuth2AuthorizedClient(value = "keycloak")
                      OAuth2AuthorizedClient client) {
        return userService.getMe(auth, client);
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