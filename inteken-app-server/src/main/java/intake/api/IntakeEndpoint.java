package intake.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class IntakeEndpoint {

    /**
     * Based on the stepped up account the thuis-instelling is deducted with the API ednpoints.
     *
     * The authenticating_authority from the introspect endpoint determines the ??
     *
     * We need to get the home institution when stepped up from eduid ALA back to the RP / SP
     *
     * In de usecase inteken zal de onderwijs aanbod site een "start inteken verzoek" doen richting de inteken app.
     * De inteken app start de oidc flow met een stepup parameter om ervoor te zorgen dat eduID ALA het account
     * linkt met een SURFconext IdP.
     *
     * Het origele verzoek om de inteken flow te starten moet resulteren in een entiyID (=OIDC clientID) van de
     * thuisinstelling om daarbij het API endpoint van deze instelling te retourneren. Hoe komt dit attribuut van eduID IdP door EB naar OIDC-NG en dan weer terug naar de inteken-app?
     *
     * @param authentication DefaultOAuth2User
     * @return Course, scopes and API endpoints to proceed
     */
    @GetMapping("/register")
    public ResponseEntity register(Authentication authentication) {
        Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
        return ResponseEntity.ok(attributes);
    }

}
