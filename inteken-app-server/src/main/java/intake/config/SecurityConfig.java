package intake.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;

import java.net.URI;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String redirectUri;
    private String acrValue;
    private InMemoryClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public SecurityConfig(InMemoryClientRegistrationRepository clientRegistrationRepository,
                          @Value("${spring.security.oauth2.client.registration.oidc.redirect-uri}") String redirectUri,
                          @Value("${oidc.account_linking_context_class_ref}") String acrValue) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.redirectUri = URI.create(redirectUri).getPath();
        this.acrValue = acrValue;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
        authorizationRequestResolver.setAuthorizationRequestCustomizer(customizer ->
                customizer.additionalParameters(Collections.singletonMap("acr_values", acrValue)));
        http
                .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2Login()
                .redirectionEndpoint().baseUri(this.redirectUri)
                .and().authorizationEndpoint()
                .authorizationRequestResolver(authorizationRequestResolver);
        ;
    }
}
