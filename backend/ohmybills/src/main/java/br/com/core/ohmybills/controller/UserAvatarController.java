package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.model.UserAvatar;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.AvatarService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

@RestController
@RequestMapping("/avatar")
public class UserAvatarController {

    private final AvatarService avatarService;

    public UserAvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> uploadAvatar(@CurrentUser UserContext user,
                                             @RequestPart("file") MultipartFile file) throws Exception {
        avatarService.save(user.userId(), file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<byte[]> getAvatar(@CurrentUser UserContext user,
                                            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) throws Exception {
        UserAvatar avatar = avatarService.get(user.userId());
        return validateAndGetImage(ifNoneMatch, avatar);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> deleteAvatar(@CurrentUser UserContext user) {
        avatarService.delete(user.userId());
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<byte[]> validateAndGetImage(String ifNoneMatch, UserAvatar avatar) throws NoSuchAlgorithmException {
        String etag = "\"" + HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(avatar.getData())) + "\"";

        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity.status(304).eTag(etag).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getContentType()))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePrivate())
                .eTag(etag)
                .body(avatar.getData());
    }
}