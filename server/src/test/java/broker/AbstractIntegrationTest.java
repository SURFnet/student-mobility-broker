package broker;

import broker.registry.InstitutionRegistry;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "service_registry.path=classpath:/service-registry-test.yml",
                "config.sis_results_endpoint=http://localhost:8081/api/play-results"
        })
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected InstitutionRegistry institutionRegistry;

    @BeforeEach
    public void before() {
        RestAssured.port = port;
    }

    public static String readFile(String path) throws IOException {
        return IOUtils.toString(new ClassPathResource(path).getInputStream(), Charset.defaultCharset());
    }
}
