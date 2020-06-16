package intake.api;

import intake.AbstractIntegrationTest;
import intake.model.Course;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest extends AbstractIntegrationTest {

    @Test
    public void me() {
        Map<String, Object> user = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/private/me")
                .as(new TypeRef<Map<String, Object>>() {
                });
        assertEquals("john.doe@ou.org", user.get("email"));
    }

    @Test
    public void sso() {
        given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .queryParam("location", "/info")
                .get("/intake/api/private/sso")
                .then()
                .statusCode(302)
                .header("Location","http://localhost:3003/info");
    }

    @Test
    public void logout() {
        Map<String, String> res = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/private/logout")
                .as(new TypeRef<Map<String, String>>() {
                });
        assertEquals("ok", res.get("status"));
    }
}