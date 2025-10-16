package broker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class BrokerApplicationTest {

    @Test
    public void main() {
        BrokerApplication.main(new String[]{"--server.port=8066"});
        RestAssured.port = 8066;
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/internal/health")
                .then()
                .body("status", equalTo("UP"));

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/internal/info")
                .then()
                .statusCode(SC_OK);
    }
}