package intake.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class IntakeEndpoint {

    private final Map<String, String> config;

    public IntakeEndpoint() {
        config = new HashMap<>();
        config.put("client_url", "http://localhost:3003");
        config.put("server_login", "http://localhost:8091/intake/api/sso");
    }

    @GetMapping("/intake/api/config")
    public ResponseEntity config() {
        return ResponseEntity.ok(config);
    }

    @GetMapping("/intake/api/sso")
    public ResponseEntity sso(@RequestParam(value = "location", required = false, defaultValue = "/") String location)
            throws URISyntaxException {
        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(config.get("client_url") + location)).build();
    }

    @GetMapping("/intake/api/me")
    public ResponseEntity me(Authentication authentication) {
        Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/intake/api/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }

}
