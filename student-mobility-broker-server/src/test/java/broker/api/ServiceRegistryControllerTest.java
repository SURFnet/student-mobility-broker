package broker.api;

import broker.AbstractIntegrationTest;
import broker.domain.Institution;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ServiceRegistryControllerTest extends AbstractIntegrationTest {

    @Test
    public void institutions() {
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
            assertNull(institution.getCourseAuthenticationClientId());
            assertNull(institution.getCourseAuthenticationPassword());
            assertNull(institution.getCourseAuthenticationSecret());
            assertNull(institution.getCourseAuthenticationUserName());
        });
    }


}
