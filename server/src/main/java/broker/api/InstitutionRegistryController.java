package broker.api;

import broker.registry.InstitutionRegistry;
import broker.domain.Institution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class InstitutionRegistryController {

    private final InstitutionRegistry institutionRegistry;

    private static final Log LOG = LogFactory.getLog(InstitutionRegistryController.class);

    public InstitutionRegistryController(InstitutionRegistry institutionRegistry) {
        this.institutionRegistry = institutionRegistry;
    }

    /*
     * Endpoint called by the playground GUI to get all institutions
     */
    @GetMapping(value = "/api/service-registry")
    public List<Institution> serviceRegistry() {
        LOG.debug("Received request for service registry.");
        return institutionRegistry.allInstitutions().stream().map(Institution::sanitize).collect(toList());
    }

    @PostMapping(value = "/api/validate-service-registry-endpoints")
    public Map<String, Boolean> validate(@RequestBody Map<String, String> enrollmentRequest) {
        LOG.debug(String.format("Validating enrollmentRequest with %s", enrollmentRequest));

        Collection<Institution> institutions = institutionRegistry.allInstitutions();
        String personURI = enrollmentRequest.get("personURI");
        boolean validPersonURI = institutions.stream().anyMatch(institution -> institution.getPersonsEndpoint().equals(personURI));
        if (!validPersonURI) {
            LOG.info(String.format("Invalid person URI '%s'. No institution with this personsEndpoint", personURI));
        }
        boolean validSchacHome = true;
        String homeInstitution = enrollmentRequest.get("homeInstitution");
        if (StringUtils.hasText(homeInstitution)) {
            validSchacHome = institutions.stream().anyMatch(institution -> institution.getSchacHome().equals(homeInstitution));
            if (!validSchacHome) {
                LOG.info(String.format("Invalid homeInstitution '%s'. No institution with this schacHome", homeInstitution));
            }
        }
        boolean validAssociationURI = true;
        String associationURI = enrollmentRequest.get("associationURI");
        if (StringUtils.hasText(associationURI)) {
            validAssociationURI = institutions.stream().anyMatch(institution -> institution.getAssociationsEndpoint().equals(associationURI));
            if (!validAssociationURI) {
                LOG.info(String.format("Invalid associationURI '%s'. No institution with this associationsEndpoint", associationURI));
            }
        }
        return Collections.singletonMap("valid", validPersonURI && validSchacHome && validAssociationURI);
    }

    @PostMapping(value = "/api/associations-uri")
    public ResponseEntity<Map<String, String>> associationsURI(@RequestBody Map<String, String> enrollmentRequest) {
        LOG.debug(String.format("Returning associationsURI for %s", enrollmentRequest));

        return findInstitution(enrollmentRequest)
                .map(institution -> ResponseEntity.ok(Collections.singletonMap("associationsURI", institution.getAssociationsEndpoint())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/api/persons-uri")
    public ResponseEntity<Map<String, String>> personsURI(@RequestBody Map<String, String> enrollmentRequest) {
        LOG.debug(String.format("Returning personsURI for %s", enrollmentRequest));

        return findInstitution(enrollmentRequest)
                .map(ins -> ResponseEntity.ok(Collections.singletonMap("personsURI", ins.getPersonsEndpoint())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private Optional<Institution> findInstitution(Map<String, String> enrollmentRequest) {
        String homeInstitution = enrollmentRequest.get("homeInstitution");
        return institutionRegistry.findInstitutionBySchacHome(homeInstitution);
    }
}
