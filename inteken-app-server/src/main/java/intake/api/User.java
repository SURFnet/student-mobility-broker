package intake.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/intake/api")
public class User {

    private final Map<String, String> config;

    public User(@Value("${config.client_url}") String clientUrl,
                @Value("${config.server_login}") String serverLogin) {
        config = new HashMap<>();
        config.put("client_url", clientUrl);
        config.put("server_login", serverLogin);
    }

    @GetMapping("/public/config")
    public ResponseEntity config() {
        return ResponseEntity.ok(config);
    }

    @GetMapping("/private/sso")
    public ResponseEntity sso(@RequestParam(value = "location", required = false, defaultValue = "/") String location)
            throws URISyntaxException {
        return ResponseEntity.status(HttpStatus.FOUND).location(new URI(config.get("client_url") + location)).build();
    }

    @GetMapping("/private/me")
    public ResponseEntity me(Authentication authentication) {
        Map<String, Object> attributes = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/private/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }

}
