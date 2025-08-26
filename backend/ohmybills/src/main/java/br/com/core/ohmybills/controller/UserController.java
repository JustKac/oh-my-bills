// Java
package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserDTO getMe(@CurrentUser UserContext user) {
        return userService.getMe(user.userId(), user.keycloakId());
    }
}