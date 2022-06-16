package broker.api;

import broker.ServiceRegistry;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class ServiceRegistryController {

    private final ServiceRegistry serviceRegistry;

    private static final Log LOG = LogFactory.getLog(ServiceRegistryController.class);

    public ServiceRegistryController(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /*
     * Endpoint called by the playground GUI to get all institutions
     */
    @GetMapping(value = "/api/service-registry")
    public List<Institution> serviceRegistry() {
        LOG.debug("Received request for service registry.");
        return serviceRegistry.allInstitutions().stream().map(Institution::sanitize).collect(toList());
    }

    @PostMapping(value = "/api/validate-service-registry-endpoints")
    public Map<String, Boolean> validate(@RequestBody Map<String, String> enrollmentRequest) {
        LOG.debug(String.format("Validating enrollmentRequest with %s", enrollmentRequest));

        List<Institution> institutions = serviceRegistry.allInstitutions();
        boolean validPersonURI = institutions.stream().anyMatch(institution -> institution.getPersonsEndpoint().equals(enrollmentRequest.get("personURI")));
        boolean validSchacHome = true;
        String homeInstitution = enrollmentRequest.get("homeInstitution");
        if (StringUtils.hasText(homeInstitution)) {
            validSchacHome = institutions.stream().anyMatch(institution -> institution.getSchacHome().equals(homeInstitution));
        }
        boolean validAssociationURI = true;
        String associationURI = enrollmentRequest.get("associationURI");
        if (StringUtils.hasText(associationURI)) {
            validAssociationURI = institutions.stream().anyMatch(institution -> institution.getAssociationsEndpoint().equals(associationURI));
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
                .map(ins -> ResponseEntity.ok(Collections.singletonMap("associationsURI", ins.getPersonsEndpoint())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private Optional<Institution> findInstitution(Map<String, String> enrollmentRequest) {
        String homeInstitution = enrollmentRequest.get("homeInstitution");
        return serviceRegistry.allInstitutions().stream()
                .filter(ins -> ins.getSchacHome().equals(homeInstitution))
                .findAny();
    }
}
