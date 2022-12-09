package broker.api;

import broker.AbstractIntegrationTest;
import broker.registry.InMemoryInstitutionRegistry;
import broker.WireMockExtension;
import broker.domain.BrokerRequest;
import broker.queue.QueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static broker.api.BrokerController.BROKER_REQUEST_SESSION_KEY;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrokerControllerUnitTest {

    private BrokerController brokerController = new BrokerController(
            "http://localhost",
            "http://localhost",
            new URI("http://localhost"),
            "client",
            "secret", "sisUser", "sisPassword", "sisResultsEndpoint",
            false,
            false,
            "utrecht.nl",
            "eindhoven.nl",
            "1",
            "https://educhange.nl/",
            20_000,
            new URI("https://eduhub.nl"),
            "eduhub",
            "secret",
            new InMemoryInstitutionRegistry(new ClassPathResource("service-registry-test.yml")),
            new QueueService("http://localhost:8082", "edubrokersurf", "http://localhost:8083/start"));

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    public BrokerControllerUnitTest() throws URISyntaxException {
    }

    @Test
    public void playground() throws IOException {
        stubFor(get(urlPathMatching("/offerings/1")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(AbstractIntegrationTest.readFile("data/offering.json"))));
        stubFor(post(urlPathMatching("/api/start")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"result\":\"ok\", \"code\":\"ok\"}")));

        Map<String, String> body = new HashMap<>();
        body.put("code", "200");
        body.put("result", "playground");

        HttpServletRequest request = new MockHttpServletRequest();
        BrokerRequest brokerRequest = new BrokerRequest();
        brokerRequest.setHomeInstitutionSchacHome("eindhoven.nl");
        brokerRequest.setGuestInstitutionSchacHome("utrecht.nl");
        brokerRequest.setOfferingId("1");

        request.getSession(true).setAttribute(BROKER_REQUEST_SESSION_KEY, brokerRequest);
        Map<String, Object> res = brokerController.start(request, body);
        assertEquals("ok", res.get("result"));

    }

    @Test
    public void start() throws IOException {
        stubFor(get(urlPathMatching("/offerings/1")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(AbstractIntegrationTest.readFile("data/offering.json"))));
        stubFor(post(urlPathMatching("/api/start")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(500)));

        Map<String, String> body = new HashMap<>();
        body.put("code", "200");
        body.put("result", "playground");

        HttpServletRequest request = new MockHttpServletRequest();
        BrokerRequest brokerRequest = new BrokerRequest();
        brokerRequest.setHomeInstitutionSchacHome("eindhoven.nl");
        brokerRequest.setGuestInstitutionSchacHome("utrecht.nl");
        brokerRequest.setOfferingId("1");

        request.getSession(true).setAttribute(BROKER_REQUEST_SESSION_KEY, brokerRequest);
        Map<String, Object> res = brokerController.start(request, body);

        assertEquals(500, res.get("code"));
        assertEquals("Server error at Utrecht University", res.get("message"));

    }

    @Test
    public void translateOfferingType() {
        BrokerRequest brokerRequest = new BrokerRequest();
        brokerRequest.setOfferingType("MINOR");
        assertEquals("program", brokerController.translateOfferingType(brokerRequest));

        brokerRequest.setOfferingType("program");
        assertEquals("program", brokerController.translateOfferingType(brokerRequest));

        brokerRequest.setOfferingType("COMPONENT");
        assertEquals("component", brokerController.translateOfferingType(brokerRequest));

        brokerRequest.setOfferingType("COURSE");
        assertEquals("course", brokerController.translateOfferingType(brokerRequest));

        brokerRequest.setOfferingType(null);
        assertEquals("course", brokerController.translateOfferingType(brokerRequest));
    }
}
