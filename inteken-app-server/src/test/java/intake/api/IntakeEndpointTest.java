package intake.api;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntakeEndpointTest {

    @LocalServerPort
    protected int port;

    @Before
    public void before() throws Exception {
        RestAssured.port = port;
    }

    @Test
    public void me() {
        Headers headers = given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/me")
                .headers();
        String location = headers.get("Location").getValue();
        assertEquals("http://localhost:" + port + "/oauth2/authorization/oidc", location);
    }

    @Test
    public void config() {
        Map<String, String> config = given().when()
                .contentType(ContentType.JSON)
                .get("/intake/api/config")
                .as(new TypeRef<Map<String, String>>() {
                });
        System.out.println(config);
    }
}