package profit.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authentication")
@RestController
@Slf4j
public class ExternalAuthenticationController {

    @PostMapping
    public ResponseEntity<String> externalAuthenticate(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();


            log.info("External authentication successful for user: {}", username);
            return ResponseEntity.ok("OK");
        }
//        log.error("External authentication failed or user not authenticated");
        return ResponseEntity.badRequest().body("Authentication failed"); //다른 코드에서 JWT 만료는 처리해서 작동 안될것.
    }
}
