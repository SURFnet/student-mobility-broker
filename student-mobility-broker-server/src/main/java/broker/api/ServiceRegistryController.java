package broker.api;

import broker.ServiceRegistry;
import broker.domain.EnrollmentRequest;
import broker.domain.Institution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public Map<String, Boolean> validate(@RequestBody EnrollmentRequest enrollmentRequest) {
        LOG.debug(String.format("Validating enrollmentRequest with %s", enrollmentRequest));

        List<Institution> institutions = serviceRegistry.allInstitutions();
        boolean validResultURI = institutions.stream().anyMatch(institution -> institution.getResultsEndpoint().equals(enrollmentRequest.getResultsURI()));
        boolean validPersonURI = institutions.stream().anyMatch(institution -> institution.getPersonsEndpoint().equals(enrollmentRequest.getPersonURI()));
        return Collections.singletonMap("valid", validPersonURI && validResultURI);
    }

}
