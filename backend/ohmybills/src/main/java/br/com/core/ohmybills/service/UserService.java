package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

public interface UserService {

    UserDTO getMe(Authentication auth,
                  @RegisteredOAuth2AuthorizedClient(value = "keycloak") OAuth2AuthorizedClient client);
}
