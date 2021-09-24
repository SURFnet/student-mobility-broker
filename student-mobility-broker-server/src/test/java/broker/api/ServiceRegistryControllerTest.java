package broker.api;

import broker.AbstractIntegrationTest;
import broker.domain.EnrollmentRequest;
import broker.domain.Institution;
import broker.domain.PersonAuthentication;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceRegistryControllerTest extends AbstractIntegrationTest {

    @Test
    void institutions() {
        List<Institution> institutions = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/service-registry")
                .as(new TypeRef<List<Institution>>() {
                });
        assertEquals(3, institutions.size());
        institutions.stream().forEach(institution -> {
            assertNull(institution.getRegistrationPassword());
            assertNull(institution.getRegistrationUser());
            assertNull(institution.getAuthenticationEndpoint());
            assertNull(institution.getResultsEndpoint());
            assertNull(institution.getPersonsEndpoint());
            assertNull(institution.getCourseAuthenticationPassword());
            assertNull(institution.getCourseAuthenticationUserName());

            assertNotNull(institution.getSchacHome());
            assertNotNull(institution.getName());
            assertNotNull(institution.getLogoURI());
            assertNotNull(institution.getAbbreviation());
            assertNotNull(institution.getCourseEndpoint());
            assertNotNull(institution.getCourseAuthentication());
            assertNotNull(institution.getScopes());
        });
    }

    @Test
    void validate() {
        Map<String, Boolean> validate = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new EnrollmentRequest(
                        "http://localhost:8081/persons/me",
                        PersonAuthentication.HEADER,
                        "http://localhost:8081/associations/me",
                        "scope"))
                .when()
                .post("/api/validate-service-registry-endpoints")
                .as(new TypeRef<Map<String, Boolean>>() {
                });
        assertTrue(validate.get("valid"));
    }

    @Test
    void invalid() {
        Map<String, Boolean> validate = given()
                .contentType(ContentType.JSON)
                .body(new EnrollmentRequest(
                        "http://localhost:8081/nope",
                        PersonAuthentication.FORM,
                        "http://localhost:8081/associations/me",
                        "scope"))
                .when()
                .post("/api/validate-service-registry-endpoints")
                .as(new TypeRef<Map<String, Boolean>>() {
                });
        assertFalse(validate.get("valid"));
    }
}
