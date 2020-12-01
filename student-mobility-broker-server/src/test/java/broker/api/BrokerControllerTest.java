package broker.api;

import broker.AbstractIntegrationTest;
import broker.WireMockExtension;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrokerControllerTest extends AbstractIntegrationTest {

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    @Test
    public void broker() throws IOException {
        SessionFilter sessionFilter = new SessionFilter();
        formSubmitByCatalog(sessionFilter);
        featureToggles();
        guiGetOffering(sessionFilter);
        startRegistration(sessionFilter);
    }

    @Test
    public void playground() {
        Map<String, Object> body = new HashMap<>();
        body.put("code", 400);
        body.put("message", "Something bad happened");
        Map<String, Object> result = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/start")
                .as(new TypeRef<Map<String, Object>>() {
                });
        assertEquals(result.keySet(), body.keySet());
    }

    private void featureToggles() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/features")
                .then()
                .body("local", equalTo(true))
                .body("allowPlayground", equalTo(true));

    }

    private void formSubmitByCatalog(SessionFilter sessionFilter) {
        given().redirects().follow(false)
                .filter(sessionFilter)
                .when()
                .param("homeInstitutionSchacHome", "eindhoven.nl")
                .param("guestInstitutionSchacHome", "utrecht.nl")
                .param("offeringID", "1")
                .post("/api/broker")
                .then()
                .header("Location", "http://localhost:3003?step=approve");
    }

    private void guiGetOffering(SessionFilter sessionFilter) throws IOException {
        stubFor(get(urlPathMatching("/offerings/1")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(readFile("data/offering.json"))));
        Map<String, Object> result = given()
                .filter(sessionFilter)
                .accept(ContentType.JSON)
                .when()
                .get("/api/offering")
                .as(new TypeRef<Map<String, Object>>() {
                });
        assertTrue(result.containsKey("homeInstitution"));
        assertTrue(result.containsKey("guestInstitution"));
        assertTrue(result.containsKey("offering"));
        assertTrue(result.containsKey("authenticationActionUrl"));

        Map<String, String> enrollmentRequest = (Map<String, String>) result.get("enrollmentRequest");
        assertEquals("http://localhost:8081/offerings/1", enrollmentRequest.get("offeringURI"));
        assertEquals("http://localhost:8081/persons/me", enrollmentRequest.get("personURI"));
        assertEquals("https://eindhoven/api inteken", enrollmentRequest.get("scope"));
    }

    private void startRegistration(SessionFilter sessionFilter) throws IOException {
        String correlationID = "123456";
        stubFor(post(urlPathMatching("/api/enrollment"))
                .withHeader("X-Correlation-ID", new EqualToPattern(correlationID))
                .withBasicAuth("user", "secret")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(readFile("data/registration-result.json"))));
        Map<String, Object> result = given()
                .filter(sessionFilter)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("correlationID", correlationID))
                .when()
                .post("/api/start")
                .as(new TypeRef<Map<String, Object>>() {
                });
        assertEquals("ok", result.get("result"));
    }


}
