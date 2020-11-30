package broker.api;

import broker.ServiceRegistry;
import broker.domain.BrokerRequest;
import broker.domain.EnrollmentRequest;
import broker.domain.Institution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BrokerController {

    public static final String BROKER_REQUEST_SESSION_KEY = "BROKER_REQUEST_SESSION_KEY";

    private final String clientUrl;

    private final ServiceRegistry serviceRegistry;
    private final RestTemplate restTemplate = new RestTemplate();
    private Map<String, Object> featureToggles = new HashMap<>();
    private final ParameterizedTypeReference<Map<String, Object>> mapRef = new ParameterizedTypeReference<Map<String, Object>>() {
    };


    public BrokerController(@Value("${config.broker_client_url}") String clientUrl,
                            @Value("${config.local}") boolean local,
                            ServiceRegistry serviceRegistry) {
        this.clientUrl = clientUrl;
        this.serviceRegistry = serviceRegistry;
        this.featureToggles.put("local", local);
    }

    /*
     * Endpoint called by the catalog form submit. Give browser-control back to the GUI
     */
    @PostMapping(value = "/api/broker", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public View brokerRequest(HttpServletRequest request, @ModelAttribute BrokerRequest brokerRequest) {
        brokerRequest.validate();
        //This establishes a session ID for the client
        request.getSession().setAttribute(BROKER_REQUEST_SESSION_KEY, brokerRequest);
        return new RedirectView(clientUrl + "?step=approve");
    }

    /*
     * Endpoint called by the GUI to get the feature toggles
     */
    @GetMapping(value = "/api/features")
    public Map<String, Object> features() {
        return featureToggles;
    }


    /*
     * Endpoint called by the GUI in first rendering
     */
    @GetMapping(value = "/api/offering")
    public Map<String, Object> offering(HttpServletRequest request) {
        BrokerRequest brokerRequest = (BrokerRequest) request.getSession().getAttribute(BROKER_REQUEST_SESSION_KEY);
        Institution guestInstitution = serviceRegistry.findInstitutionBySchacHome(brokerRequest.getGuestInstitutionSchacHome());
        Institution homeInstitution = serviceRegistry.findInstitutionBySchacHome(brokerRequest.getHomeInstitutionSchacHome());
        String offeringURI = String.format("%s/%s", homeInstitution.getCourseEndpoint(), brokerRequest.getOfferingID());
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest(offeringURI, homeInstitution.getPersonsEndpoint(), homeInstitution.getScopes());

        Map<String, Object> result = new HashMap<>();
        result.put("guestInstitution", guestInstitution.sanitize());
        result.put("homeInstitution", homeInstitution.sanitize());
        result.put("authenticationActionUrl", homeInstitution.getRegistrationEndpoint() + "/api/enrollment");
        result.put("enrollmentRequest", enrollmentRequest);
        result.put("offering", fetchOffering(homeInstitution, brokerRequest));
        return result;
    }

    /*
     * Proxy the call to the actual registration of the enrollment for the user
     */
    @PostMapping("/api/start")
    public Map<String, Object> start(HttpServletRequest request, @RequestBody Map<String, String> correlationMap) {
        BrokerRequest brokerRequest = (BrokerRequest) request.getSession().getAttribute(BROKER_REQUEST_SESSION_KEY);
        Institution homeInstitution = serviceRegistry.findInstitutionBySchacHome(brokerRequest.getHomeInstitutionSchacHome());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", correlationMap.get("correlationID"));
        headers.setBasicAuth(homeInstitution.getRegistrationUser(), homeInstitution.getRegistrationPassword());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        String url = homeInstitution.getRegistrationEndpoint() + "/api/start";
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, mapRef);
        return responseEntity.getBody();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchOffering(Institution homeInstitution, BrokerRequest brokerRequest) {
        return restTemplate.getForEntity(String.format("%s/%s",
                homeInstitution.getCourseEndpoint(),
                brokerRequest.getOfferingID()), Map.class).getBody();
    }

}
