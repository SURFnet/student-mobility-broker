package intake.api;

import exception.NoScopedAffiliationException;
import intake.model.Course;
import intake.model.Institution;
import intake.model.Registration;
import intake.repository.CatalogRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/intake/api", produces = APPLICATION_JSON_VALUE)
public class Education {

    private static final Log LOG = LogFactory.getLog(Education.class);

    private final CatalogRepository catalogRepository;

    public Education(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @GetMapping("/public/institutions")
    public List<Institution> institutions() {
        return catalogRepository.institutions();
    }

    @GetMapping("/public/institutions-schac-home")
    public List<String> institutionsSchacHomes() {
        return catalogRepository.institutions().stream().map(Institution::getSchacHome).collect(Collectors.toList());
    }

    @GetMapping("/public/institution")
    public Institution findInstitutionBySchacHome(@RequestParam("schac_home") String schacHome) {
        return catalogRepository.findInstitutionBySchacHome(schacHome);
    }

    @GetMapping("/public/course")
    public Course findCourseByIdentifier(@RequestParam("identifier") String identifier) {
        return catalogRepository.findCourseByIdentifier(identifier);
    }

    @PutMapping("/private/register")
    @SuppressWarnings("unchecked")
    public ResponseEntity register(Authentication authentication, @RequestBody Registration registration) {
        LOG.info(String.format("Registration %s URL for  %s", registration, authentication.getPrincipal()));

        //The user has to have a scoped affiliation for the schacHomeInstitution
        List<String> eduPersonScopedAffiliations = (List<String>) ((DefaultOAuth2User) authentication.getPrincipal())
                .getAttributes()
                .getOrDefault("eduperson_scoped_affiliation", new ArrayList<String>());

        eduPersonScopedAffiliations.stream()
                .filter(aff -> aff.endsWith(registration.getSchacHomeInstitution()))
                .findFirst()
                .orElseThrow(NoScopedAffiliationException::new);

        Course course = catalogRepository.findCourseByIdentifier(registration.getCourseIdentifier());
        Institution homeInstitution = catalogRepository.findInstitutionBySchacHome(registration.getSchacHomeInstitution());
        Institution guestInstitution = catalogRepository.findInstitutionBySchacHome(registration.getSchacHomeGuestInstitution());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(guestInstitution.getApiBaseUrl());
        builder.queryParam("course", course.getIdentifier());
        builder.queryParam("scope", course.getRequiredScopes().stream().collect(Collectors.joining(" ")));
        builder.queryParam("apiUrl", homeInstitution.getApiBaseUrl());

        return registration.isPreview() ?
                ResponseEntity.ok(Collections.singletonMap("url", builder.build().toUriString())) :
                ResponseEntity.status(HttpStatus.FOUND).location(builder.build().toUri()).build();
    }


}
