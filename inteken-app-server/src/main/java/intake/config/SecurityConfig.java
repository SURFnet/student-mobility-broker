package intake.config;

import com.nimbusds.openid.connect.sdk.ClaimsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final String redirectUri;
    private final String acrValue;
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public SecurityConfig(InMemoryClientRegistrationRepository clientRegistrationRepository,
                          Environment environment,
                          @Value("${spring.security.oauth2.client.registration.oidc.redirect-uri}") String redirectUri,
                          @Value("${oidc.account_linking_context_class_ref}") String acrValue) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.environment = environment;
        this.redirectUri = URI.create(redirectUri).getPath();
        this.acrValue = acrValue;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/actuator/**", "/intake/api/public/**", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
        authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());

        http.csrf().disable()
                .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2Login()
                .redirectionEndpoint().baseUri(this.redirectUri)
                .and().authorizationEndpoint()
                .authorizationRequestResolver(authorizationRequestResolver);

        if (environment.acceptsProfiles(Profiles.of("test"))) {
            http.addFilterBefore(new MockAuthorizationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        }
    }

    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
        return customizer -> {
            Map<String, Object> additionalParameters = new HashMap<>();
            ClaimsRequest claimsRequest = new ClaimsRequest();
            Arrays.asList(
                    "eduperson_principal_name",
                    "eduperson_scoped_affiliation",
                    "email",
                    "family_name",
                    "given_name",
                    "eduid",
                    "preferred_username",
                    "schac_home_organization"
            ).stream().forEach(claimsRequest::addIDTokenClaim);

            //This is the enforce account linking by eduID
            additionalParameters.put("acr_values", acrValue);
            //This prevents us from calling the userinfo endpoint
            additionalParameters.put("claims", claimsRequest.toString());
            //Otherwise we stick to oauth2 instead of oidc
            customizer.scope("openid");
            customizer.additionalParameters(additionalParameters);
        };
    }
}
