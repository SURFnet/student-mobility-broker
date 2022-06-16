package broker.api;

import broker.AbstractIntegrationTest;
import broker.domain.EnrollmentRequest;
import broker.domain.Institution;
import broker.domain.PersonAuthentication;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

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
            assertNull(institution.getAssociationsEndpoint());
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
                        "http://localhost:8081/associations",
                        "utrecht.nl",
                        "scope"))
                .when()
                .post("/api/validate-service-registry-endpoints")
                .as(new TypeRef<Map<String, Boolean>>() {
                });
        assertTrue(validate.get("valid"));
    }

    @Test
    void validateWithoutHomeInstitution() {
        Map<String, Boolean> validate = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new EnrollmentRequest(
                        "http://localhost:8081/persons/me",
                        PersonAuthentication.HEADER,
                        "http://localhost:8081/associations",
                        null,
                        "scope"))
                .when()
                .post("/api/validate-service-registry-endpoints")
                .as(new TypeRef<Map<String, Boolean>>() {
                });
        assertTrue(validate.get("valid"));
    }

    @Test
    void validateWithoutAssociationURI() {
        Map<String, Boolean> validate = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(new EnrollmentRequest(
                        "http://localhost:8081/persons/me",
                        PersonAuthentication.HEADER,
                        null,
                        "utrecht.nl",
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
                        "http://localhost:8081/persons/me",
                        PersonAuthentication.FORM,
                        "http://localhost:8081/associations/me",
                        "nope",
                        "scope"))
                .when()
                .post("/api/validate-service-registry-endpoints")
                .as(new TypeRef<Map<String, Boolean>>() {
                });
        assertFalse(validate.get("valid"));
    }

    @Test
    void associationsURI() {
        Map<String, String> result = given()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("homeInstitution","utrecht.nl"))
                .when()
                .post("/api/associations-uri")
                .as(new TypeRef<Map<String, String>>() {
                });
        assertEquals("http://localhost:8081/associations", result.get("associationsURI"));
    }

    @Test
    void associationsURIInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("homeInstitution","nope"))
                .when()
                .post("/api/associations-uri")
                .then()
                .statusCode(404);
    }

    @Test
    void personsURI() {
        Map<String, String> result = given()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("homeInstitution","utrecht.nl"))
                .when()
                .post("/api/persons-uri")
                .as(new TypeRef<Map<String, String>>() {
                });
        assertEquals("http://localhost:8081/persons/me", result.get("personsURI"));
    }

    @Test
    void personsURIInvalid() {
        given()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("homeInstitution", "nope"))
                .when()
                .post("/api/persons-uri")
                .then()
                .statusCode(404);
    }
}
