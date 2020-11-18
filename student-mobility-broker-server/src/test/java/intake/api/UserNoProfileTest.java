package intake.api;

import intake.AbstractIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles(value = "prod", inheritProfiles = false)
public class UserNoProfileTest extends AbstractIntegrationTest {

    @Test
    public void me() {
        Headers headers = given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/private/me")
                .headers();
        String location = headers.get("Location").getValue();
        assertEquals("http://localhost:" + port + "/oauth2/authorization/oidc", location);

        headers = given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .get(location)
                .headers();
        location = headers.get("Location").getValue();
        assertTrue(location.indexOf("acr_values=https://eduid.nl/trust/linked-institution") > 0);
    }

    @Test
    public void config() {
        Map<String, String> config = given().when()
                .contentType(ContentType.JSON)
                .get("/intake/api/public/config")
                .as(new TypeRef<Map<String, String>>() {
                });
        assertEquals("http://localhost:3003", config.get("client_url"));
    }
}