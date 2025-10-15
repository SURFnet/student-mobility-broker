package broker.api;

import broker.LanguageFilter;
import broker.domain.BrokerRequest;
import broker.domain.CourseAuthentication;
import broker.domain.EnrollmentRequest;
import broker.domain.Institution;
import broker.exception.RemoteException;
import broker.queue.QueueService;
import broker.registry.InstitutionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.ACCEPT;

@RestController
public class BrokerController {

    public static final String BROKER_REQUEST_SESSION_KEY = "BROKER_REQUEST_SESSION_KEY";
    public static final String OFFERING_SESSION_KEY = "OFFERING_SESSION_KEY";

    private static final Log LOG = LogFactory.getLog(BrokerController.class);

    private final String clientUrl;

    private final InstitutionRegistry institutionRegistry;
    private final QueueService queueService;
    private final RestTemplate restTemplate;
    private final Map<String, Object> featureToggles = new HashMap<>();
    private final URI tokenEndpoint;
    private final String clientId;
    private final String secret;
    private final String sisUser;
    private final String sisPassword;
    private final String sisResultsEndpoint;
    private final URI eduHubGatewayUrl;
    private final String eduHubUser;
    private final String eduHubPassword;

    private final ParameterizedTypeReference<Map<String, Object>> mapRef = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    public BrokerController(@Value("${config.broker_client_url}") String clientUrl,
                            @Value("${config.start_broker_endpoint}") String startBrokerEndpoint,
                            @Value("${config.oauth2.token_endpoint}") URI tokenEndpoint,
                            @Value("${config.oauth2.client_id}") String clientId,
                            @Value("${config.oauth2.secret}") String secret,
                            @Value("${config.sis_user}") String sisUser,
                            @Value("${config.sis_password}") String sisPassword,
                            @Value("${config.sis_results_endpoint}") String sisResultsEndpoint,
                            @Value("${config.local}") boolean local,
                            @Value("${config.allow_playground}") boolean allowPlayground,
                            @Value("${config.poll_enabled}") boolean pollEnabled,
                            @Value("${config.survey_enabled}") boolean surveyEnabled,
                            @Value("${config.poll_survey}") URI pollSurvey,
                            @Value("${config.play_home_institution_schacHome}") String playHomeInstitutionSchacHome,
                            @Value("${config.play_guest_institution_schacHome}") String playGuestInstitutionSchacHome,
                            @Value("${config.play_offering_id}") String playOfferingID,
                            @Value("${config.catalog_url}") String catalogUrl,
                            @Value("${config.connection_timeout_millis}") int connectionTimeoutMillis,
                            @Value("${config.edu_hub.gateway_url}") URI eduHubGatewayUrl,
                            @Value("${config.edu_hub.user}") String eduHubUser,
                            @Value("${config.edu_hub.password}") String eduHubPassword,
                            InstitutionRegistry institutionRegistry,
                            QueueService queueService) {
        this.clientUrl = clientUrl;
        this.tokenEndpoint = tokenEndpoint;
        this.clientId = clientId;
        this.secret = secret;
        this.sisUser = sisUser;
        this.sisPassword = sisPassword;
        this.sisResultsEndpoint = sisResultsEndpoint;
        this.institutionRegistry = institutionRegistry;
        this.queueService = queueService;
        this.eduHubGatewayUrl = eduHubGatewayUrl;
        this.eduHubUser = eduHubUser;
        this.eduHubPassword = eduHubPassword;
        this.featureToggles.put("startBrokerEndpoint", startBrokerEndpoint);
        this.featureToggles.put("local", local);
        this.featureToggles.put("allowPlayground", allowPlayground);
        this.featureToggles.put("catalogUrl", catalogUrl);
        this.featureToggles.put("queue", queueService.getBaseUrl());
        this.featureToggles.put("pollEnabled", pollEnabled);
        this.featureToggles.put("surveyEnabled", surveyEnabled);
        this.featureToggles.put("pollSurvey", pollSurvey);

        if (allowPlayground) {
            this.featureToggles.put("playHomeInstitutionSchacHome", playHomeInstitutionSchacHome);
            this.featureToggles.put("playGuestInstitutionSchacHome", playGuestInstitutionSchacHome);
            this.featureToggles.put("offeringId", playOfferingID);
        }
        this.restTemplate = new RestTemplate(getClientHttpRequestFactory(connectionTimeoutMillis));
        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("Accept-Language", LanguageFilter.language.get());
            return execution.execute(request, body);
        }));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        String[] denyList = new String[]{"class.*", "Class.*", "*.class.*", "*.Class.*"};
        dataBinder.setDisallowedFields(denyList);
    }

    /*
     * Endpoint called by the external catalog form submit. Give browser-control back to the GUI, but check if we need
     * to redirect to queue-it
     */
    @PostMapping(value = "/api/broker", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public View brokerRequest(HttpServletRequest request,
                              @ModelAttribute BrokerRequest brokerRequest,
                              @RequestParam(value = "play", required = false, defaultValue = "false") boolean play)
            throws UnsupportedEncodingException {
        LOG.debug("Called by the external catalog form submit with BrokerRequest " + brokerRequest);
        Institution guestInstitution;
        try {
            //we want to fail fast
            brokerRequest.validate();
            this.getInstitution(brokerRequest.getHomeInstitutionSchacHome());
            guestInstitution = this.getInstitution(brokerRequest.getGuestInstitutionSchacHome());
        } catch (IllegalArgumentException e) {
            LOG.warn("Validation error in the brokerRequest: " + brokerRequest, e);
            return new RedirectView(clientUrl + "?error=400");
        } catch (RemoteException e) {
            LOG.warn("RemoteException error in the brokerRequest: " + brokerRequest, e);
            return new RedirectView(clientUrl + "?error=" + e.getRawStatusCode());
        }
        //Store if we need to redirect to queue-it
        brokerRequest.setUseQueueIt(guestInstitution.isUseQueueIt());
        //Prevent a very smart hack
        brokerRequest.setQueueItSucceeded(false);
        //This establishes a session ID for the client
        request.getSession().setAttribute(BROKER_REQUEST_SESSION_KEY, brokerRequest);

        LOG.debug(String.format("Started session %s for brokerRequest: %s", request.getSession().getId(), brokerRequest));

        String queryParams = play ? "?step=enroll&name=Johanna&correlationID=1" : "?step=approve";
        if (guestInstitution.isUseQueueIt()) {
            queryParams += "&q=" + URLEncoder.encode(queueService.getRedirectUrl(guestInstitution),
                    Charset.defaultCharset());
        }
        if (StringUtils.hasText(brokerRequest.getAlliance())) {
            queryParams += "&alliance="+ URLEncoder.encode(brokerRequest.getAlliance(),
                    Charset.defaultCharset());
        }
        return new RedirectView(clientUrl + queryParams);
    }

    /*
     * Redirect called by queue-it after the student has waited long enough
     */
    @GetMapping(value = "/api/queue/redirect")
    public View queueRedirect(HttpServletRequest request,
                              @RequestParam("queueittoken") String queueItToken) {
        LOG.debug("Redirect from queue-it: " + queueItToken);
        BrokerRequest brokerRequest = getBrokerRequest(request, false);
        Institution institution = this.getInstitution(brokerRequest.getGuestInstitutionSchacHome());
        if (!queueService.validateQueueToken(institution, queueItToken)) {
            return new RedirectView(clientUrl + "?error=407");
        }
        brokerRequest.setQueueItSucceeded(true);
        request.getSession().setAttribute(BROKER_REQUEST_SESSION_KEY, brokerRequest);
        return new RedirectView(clientUrl + "?step=approve");
    }

    /*
     * Endpoint called by the GUI to get the feature toggles
     */
    @GetMapping(value = "/api/features")
    public Map<String, Object> features() {
        LOG.debug("Received request for feature toggles.");
        return featureToggles;
    }

    /*
     * Endpoint called by the GUI in first rendering
     */
    @GetMapping(value = "/api/offering")
    public Map<String, Object> offering(HttpServletRequest request) {
        BrokerRequest brokerRequest = getBrokerRequest(request, true);
        LOG.debug(String.format("Received request for offering for brokerRequest %s and session %s",
                brokerRequest, request.getSession().getId()));

        Institution guestInstitution = getInstitution(brokerRequest.getGuestInstitutionSchacHome());
        Institution homeInstitution = getInstitution(brokerRequest.getHomeInstitutionSchacHome());
        Map<String, Object> offering;
        try {
            offering = fetchOffering(guestInstitution, brokerRequest);
        } catch (RuntimeException e) {
            LOG.error("Error in fetching offering from " + guestInstitution.getName(), e);
            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException ex = (HttpClientErrorException) e;
                status = ex.getStatusCode();
                LOG.error("Response error in fetching offering: " + ex.getResponseBodyAsString(), ex);
            }
            RemoteException remoteException = new RemoteException(status, guestInstitution.getName(), e);
            LOG.error("Reference number for client error correlation: " + remoteException.getReference());
            throw remoteException;
        }

        //Save the offering as we need it when starting the actual registration
        request.getSession().setAttribute(OFFERING_SESSION_KEY, offering);

        EnrollmentRequest enrollmentRequest = new EnrollmentRequest(
                homeInstitution.getPersonsEndpoint(),
                homeInstitution.getPersonAuthentication(),
                homeInstitution.getAssociationsEndpoint(),
                homeInstitution.getSchacHome(),
                homeInstitution.getScopes());

        Map<String, Object> result = new HashMap<>();
        result.put("guestInstitution", guestInstitution.sanitize());
        result.put("homeInstitution", homeInstitution.sanitize());
        result.put("authenticationActionUrl", guestInstitution.getAuthenticationEndpoint());
        result.put("enrollmentRequest", enrollmentRequest);
        result.put("offering", offering);
        return result;
    }

    private BrokerRequest getBrokerRequest(HttpServletRequest request, boolean validateQueueIt) {
        BrokerRequest brokerRequest = (BrokerRequest) request.getSession().getAttribute(BROKER_REQUEST_SESSION_KEY);
        if (brokerRequest == null) {
            RemoteException remoteException = new RemoteException(HttpStatus.NOT_FOUND, "No broker request in the session");
            LOG.error("Reference number for client error correlation: " + remoteException.getReference());
            throw remoteException;
        }
        if (validateQueueIt && brokerRequest.isUseQueueIt() && !brokerRequest.isQueueItSucceeded()) {
            RemoteException remoteException = new RemoteException(HttpStatus.CONFLICT, "QueueIT required but not performed");
            LOG.error("Reference number for client error correlation: " + remoteException.getReference());
            throw remoteException;
        }
        return brokerRequest;
    }

    private Institution getInstitution(String institutionSchacHome) {
        LOG.debug("Lookup institution " + institutionSchacHome + " in serviceregistry.");
        return institutionRegistry
                .findInstitutionBySchacHome(institutionSchacHome)
                .orElseThrow(() -> {
                    RemoteException remoteException = new RemoteException(HttpStatus.NOT_FOUND, institutionSchacHome);
                    LOG.error(String.format("Institution %s unknown with reference number %s", institutionSchacHome, remoteException.getReference()));
                    return remoteException;
                });
    }

    /*
     * Only allowed in playground modus. Proxy and mimic the call that normally the SIS issues to POST back results of
     * the enrollments to the home institution
     */
    @PostMapping("/api/results")
    public ResponseEntity<Map<String, Object>> results(@RequestBody Map<String, Object> message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", (String) message.get("correlationID"));
        headers.setBasicAuth(sisUser, sisPassword);
        return ResponseEntity.ok(this.exchange(sisResultsEndpoint, HttpMethod.POST, new HttpEntity<>(message, headers)));
    }

    /*
     * Only allowed in playground mode. Proxy and mimic the call that normally the SIS issues to get person information.
     */
    @PostMapping("/api/me")
    public ResponseEntity<Map<String, Object>> me(@RequestBody Map<String, Object> message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", (String) message.get("correlationID"));
        headers.setBasicAuth(sisUser, sisPassword);
        //playground hacky
        return ResponseEntity.ok(this.exchange(sisResultsEndpoint.replace("play-results", "me"),
                HttpMethod.GET, new HttpEntity<>(headers)));
    }

    /*
     * Proxy the call to the actual registration of the enrollment for the user
     */
    @PostMapping("/api/start")
    public Map<String, Object> start(HttpServletRequest request, @RequestBody Map<String, String> correlationMap) {
        BrokerRequest brokerRequest = getBrokerRequest(request, true);
        boolean crossInstitutionRequest = !brokerRequest.getHomeInstitutionSchacHome().equals(brokerRequest.getGuestInstitutionSchacHome());
        if ((boolean) this.featureToggles.get("allowPlayground") && correlationMap.keySet().stream().anyMatch(s -> s.equals("code"))) {
            LOG.debug("Returning playground request with parameters: " + correlationMap);
            HashMap<String, Object> results = new HashMap<>(correlationMap);
            results.put("crossInstitutionRequest", crossInstitutionRequest);
            return results;
        }
        Map<String, Object> offering = (Map<String, Object>) request.getSession().getAttribute(OFFERING_SESSION_KEY);

        LOG.debug(String.format("Received start registration request for brokerRequest: %s and session: %s",
                brokerRequest, request.getSession().getId()));

        try {
            Map<String, Object> body = doStart(brokerRequest, offering, correlationMap);
            request.getSession().invalidate();

            LOG.debug(String.format("Returning start registration response %s for brokerRequest: %s and session: %s",
                    body, brokerRequest, request.getSession().getId()));
            //Might be an immutable Map
            body = new HashMap<>(body);
            body .put("crossInstitutionRequest", crossInstitutionRequest);
            return body;
        } catch (HttpStatusCodeException | ResourceAccessException e) {
            HttpStatus statusCode = e instanceof HttpStatusCodeException ? ((HttpStatusCodeException)e).getStatusCode() : HttpStatus.REQUEST_TIMEOUT;
            RemoteException remoteException = new RemoteException(statusCode, e.getMessage(), e);

            String body = e instanceof HttpStatusCodeException ? ((HttpStatusCodeException)e).getResponseBodyAsString() : e.getMessage();
            LOG.error(String.format("Unexpected exception from /api/start: %s, reference number %s",
                    body, remoteException.getReference()));

            Map<String, Object> res = new HashMap<>();
            res.put("error", true);
            res.put("code", statusCode.value());
            res.put("reference", remoteException.getReference());
            return res;
        }
    }

    private Map<String, Object> doStart(BrokerRequest brokerRequest, Map<String, Object> offering, Map<String, String> correlationMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", correlationMap.get("correlationID"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        Institution guestInstitution = getInstitution(brokerRequest.getGuestInstitutionSchacHome());
        headers.setBasicAuth(guestInstitution.getRegistrationUser(), guestInstitution.getRegistrationPassword());
        HttpEntity<?> requestEntity = new HttpEntity<>(offering, headers);
        String url = guestInstitution.getRegistrationEndpoint().toString();

        LOG.debug(String.format("Start registration by POST-ing to %s", url));

        return this.exchange(url, HttpMethod.POST, requestEntity, HttpStatus.NOT_FOUND);
    }

    protected String translateOfferingType(BrokerRequest brokerRequest) {
        String type = brokerRequest.getOfferingType();
        String expandable = StringUtils.hasText(type) ? type.toLowerCase() : "course";
        if (expandable.equals("minor")) {
            expandable = "program";
        }
        return expandable;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fetchOffering(Institution guestInstitution, BrokerRequest brokerRequest) {
        if (guestInstitution.isUseEduHubForOffering()) {
            String uri = String.format("%s/%s/%s?expand=academicSession,%s",
                    this.eduHubGatewayUrl,
                    "offerings",
                    brokerRequest.getOfferingId(),
                    this.translateOfferingType(brokerRequest));
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(this.eduHubUser, this.eduHubPassword);
            headers.set(ACCEPT, "application/json;version=5");
            headers.add("X-Route", "endpoint=" + guestInstitution.getSchacHome());

            LOG.debug("Fetching offering " + uri + " with basic authentication from eduHub");
            Map<String, Object> body = this.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers));
            return (Map<String, Object>) ((Map<String, Object>) body.get("responses")).get(guestInstitution.getSchacHome());
        } else {
            String uri = String.format("%s/%s?expand=academicSession,%s",
                    guestInstitution.getCourseEndpoint(),
                    brokerRequest.getOfferingId(),
                    this.translateOfferingType(brokerRequest));
            CourseAuthentication courseAuthentication = guestInstitution.getCourseAuthentication();

            LOG.debug(String.format("Fetching offering from %s with security %s", uri, courseAuthentication.name()));

            if (courseAuthentication.equals(CourseAuthentication.NONE)) {
                LOG.debug("Fetching offering " + uri + " without authentication");
                return this.exchange(uri, HttpMethod.GET, null);
            } else if (courseAuthentication.equals(CourseAuthentication.BASIC)) {
                LOG.debug("Fetching offering " + uri + " with basic authentication");
                return this.exchange(uri, HttpMethod.GET, new HttpEntity<>(basicAuthHeaders(guestInstitution)));
            } else {
                LOG.debug("Fetching offering " + uri + " with OAUTH authentication");
                return this.exchange(uri, HttpMethod.GET, new HttpEntity<>(accessTokenHeaders()));
            }
        }
    }

    private Map<String, Object> exchange(String uri,
                                         HttpMethod method,
                                         @Nullable HttpEntity<?> requestEntity,
                                         @Nullable HttpStatus... ignoreHtpStatusCodes) {
        try {
            return restTemplate.exchange(uri, method, requestEntity, mapRef).getBody();
        } catch (HttpClientErrorException e) {
            if (ignoreHtpStatusCodes != null && Arrays.stream(ignoreHtpStatusCodes).anyMatch(httpStatus -> httpStatus.equals(e.getStatusCode()))) {
                LOG.warn(String.format("Error from uri %s with requestEntity %s and response %s", uri, requestEntity, e.getResponseBodyAsString()));
            } else {
                LOG.error(String.format("Error from uri %s with requestEntity %s and response %s", uri, requestEntity, e.getResponseBodyAsString()), e);
            }
            throw e;
        }
    }

    private HttpHeaders basicAuthHeaders(Institution institution) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(institution.getCourseAuthenticationUserName(), institution.getCourseAuthenticationPassword());
        return headers;
    }

    //we don't cache tokens, as the hit-ratio would be extremely low
    private HttpHeaders accessTokenHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, secret);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "openid");
        Map result = this.exchange(this.tokenEndpoint.toString(), HttpMethod.POST, new HttpEntity<>(body, headers));
        String accessToken = (String) result.get("access_token");

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory(int connectionTimeoutMillis) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeoutMillis);
        clientHttpRequestFactory.setReadTimeout(connectionTimeoutMillis);
        return clientHttpRequestFactory;
    }
}
