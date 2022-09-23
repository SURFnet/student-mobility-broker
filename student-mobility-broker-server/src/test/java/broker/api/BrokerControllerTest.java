package broker.api;

import broker.AbstractIntegrationTest;
import broker.WireMockExtension;
import broker.domain.CourseAuthentication;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class BrokerControllerTest extends AbstractIntegrationTest {

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    @Test
    public void brokerOfferingNoAuth() throws IOException {
        happyFlow("utrecht.nl", CourseAuthentication.NONE);
    }

    @Test
    public void brokerOfferingBasicAuth() throws IOException {
        happyFlow("eindhoven.nl", CourseAuthentication.BASIC);
    }

    @Test
    public void brokerOfferingAuthOAuth2() throws IOException {
        happyFlow("wageningen.nl", CourseAuthentication.OAUTH2);
    }

    @Test
    public void brokerOfferingInvalid() {
        SessionFilter sessionFilter = new SessionFilter();
        formSubmitByCatalog(sessionFilter, "utrecht.nl");
        featureToggles();
        Map<String, Object> result = guiGetOfferingInvalid(sessionFilter);
        assertEquals(400, result.get("status"));
    }

    @Test
    public void noSession() {
        Map<String, Object> results = given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/offering")
                .as(new TypeRef<Map<String, Object>>() {
                });
        assertEquals(404, results.get("status"));
    }

    @Test
    public void invalidStartRegistration() throws IOException {
        SessionFilter sessionFilter = new SessionFilter();
        formSubmitByCatalog(sessionFilter, "utrecht.nl");
        featureToggles();
        guiGetOffering(sessionFilter, CourseAuthentication.NONE);
        Map<String, Object> results = startRegistrationInvalid(sessionFilter);
        assertEquals(500, results.get("code"));

    }

    private void happyFlow(String guestInstitutionSchacHome, CourseAuthentication courseAuthentication) throws IOException {
        SessionFilter sessionFilter = new SessionFilter();
        formSubmitByCatalog(sessionFilter, guestInstitutionSchacHome);
        featureToggles();
        guiGetOffering(sessionFilter, courseAuthentication);
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

    @Test
    public void exceptionHandlingServiceRegistry() {
        given().redirects().follow(false)
                .when()
                .param("homeInstitutionSchacHome", "nope")
                .param("guestInstitutionSchacHome", "nope")
                .param("offeringID", "1")
                .param("type", "course")
                .post("/api/broker")
                .then()
                .header("Location", "http://localhost:3003?error=Institution+nope+unknown");
    }

    @Test
    public void playgroundBroker() {
        given().redirects().follow(false)
                .when()
                .param("homeInstitutionSchacHome", "eindhoven.nl")
                .param("guestInstitutionSchacHome", "utrecht.nl")
                .param("offeringID", "1")
                .param("type", "course")
                .queryParam("play", true)
                .post("/api/broker")
                .then()
                .header("Location", "http://localhost:3003?step=enroll&name=Johanna&correlationID=1");
    }

    @Test
    public void playResults() {
        Map<String, String> body = new HashMap<>();
        body.put("X-Correlation-ID", "some");
        body.put("result", "some");
        stubFor(post(urlPathMatching("/api/play-results")).willReturn(aResponse()
                .withBody("{\"result\":\"ok\"}")
                .withHeader("Content-type", "application/json")
                .withStatus(200)));

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .post("/api/results")
                .then()
                .statusCode(200)
                .body("result", equalTo("ok"));
    }

    @Test
    public void playMe() {
        Map<String, String> body = new HashMap<>();
        body.put("X-Correlation-ID", "some");
        stubFor(get(urlPathMatching("/api/me")).willReturn(aResponse()
                .withBody("{\"result\":\"ok\"}")
                .withHeader("Content-type", "application/json")
                .withStatus(200)));

        given()
                .when()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .post("/api/me")
                .then()
                .statusCode(200)
                .body("result", equalTo("ok"));
    }

    @Test
    public void exceptionHandlingValidation() {
        given().redirects().follow(false)
                .when()
                .param("offeringID", "1")
                .post("/api/broker")
                .then()
                .header("Location", "http://localhost:3003?error=invalid_request&details=homeInstitutionSchacHome+is+required");
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

    private void formSubmitByCatalog(SessionFilter sessionFilter, String guestInstitutionSchacHome) {
        given().redirects().follow(false)
                .filter(sessionFilter)
                .when()
                .param("homeInstitutionSchacHome", "eindhoven.nl")
                .param("guestInstitutionSchacHome", guestInstitutionSchacHome)
                .param("offeringID", "1")
                .param("type", "course")
                .post("/api/broker")
                .then()
                .header("Location", "http://localhost:3003?step=approve");
    }

    private void guiGetOffering(SessionFilter sessionFilter, CourseAuthentication courseAuthentication) throws IOException {
        MappingBuilder mappingBuilder = get(urlPathMatching("/offerings/1"));

        if (courseAuthentication.equals(CourseAuthentication.BASIC)) {
            mappingBuilder = mappingBuilder.withBasicAuth("user", "secret");
        } else if (courseAuthentication.equals(CourseAuthentication.OAUTH2)) {
            stubFor(post(urlPathMatching("/oidc/token")).willReturn(aResponse()
                    .withHeader("Content-type", "application/json")
                    .withBody("{\"access_token\":\"123456\"}")));
            mappingBuilder = mappingBuilder.withHeader(AUTHORIZATION, new EqualToPattern("Bearer " + "123456"));
        }

        stubFor(mappingBuilder.willReturn(aResponse()
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
        assertEquals("http://localhost:8081/persons/me", enrollmentRequest.get("personURI"));
        assertEquals("https://eindhoven/api inteken", enrollmentRequest.get("scope"));
    }

    private Map<String, Object> guiGetOfferingInvalid(SessionFilter sessionFilter) {
        stubFor(get(urlPathMatching("/offerings/1")).willReturn(aResponse()
                .withStatus(400)));

        return given()
                .filter(sessionFilter)
                .accept(ContentType.JSON)
                .when()
                .get("/api/offering")
                .as(new TypeRef<Map<String, Object>>() {
                });
    }

    private void startRegistration(SessionFilter sessionFilter) throws IOException {
        String correlationID = "123456";
        stubFor(post(urlPathMatching("/api/start"))
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

    private Map<String, Object> startRegistrationInvalid(SessionFilter sessionFilter) throws IOException {
        String correlationID = "123456";
        stubFor(post(urlPathMatching("/api/start"))
                .withHeader("X-Correlation-ID", new EqualToPattern(correlationID))
                .withBasicAuth("user", "secret")
                .willReturn(aResponse()
                        .withStatus(500)));

        return given()
                .filter(sessionFilter)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("correlationID", correlationID))
                .when()
                .post("/api/start")
                .as(new TypeRef<Map<String, Object>>() {
                });
    }

}
