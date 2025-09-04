package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.MainPageDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.MainPageServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/main-page")
public class MainPageController {

    private final MainPageServiceImpl mainPageService;

    public MainPageController(MainPageServiceImpl mainPageService) {
        this.mainPageService = mainPageService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public MainPageDTO getAvatar(@CurrentUser UserContext user, @RequestParam YearMonth yearMonth) {
        return mainPageService.getMainPageInfo(user.userId(), yearMonth);
    }
}