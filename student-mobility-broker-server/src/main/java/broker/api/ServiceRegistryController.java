package broker.api;

import broker.ServiceRegistry;
import broker.domain.Institution;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
public class ServiceRegistryController {

    private final ServiceRegistry serviceRegistry;

    public ServiceRegistryController(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /*
     * Endpoint called by the playground GUI to get all institutions
     */
    @GetMapping(value = "/api/service-registry")
    public List<Institution> serviceRegistry() {
        return serviceRegistry.allInstitutions().stream().map(Institution::sanitize).collect(toList());
    }

}
