package broker.api;

import broker.AbstractIntegrationTest;
import broker.WireMockExtension;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/*
 * True end-to-end test spanning both real services: boots a real generiek process (started with
 * config.local=true, so it bypasses the real SURFconext round-trip). Drives real form data into the
 * broker's own /api/broker/manual-enrolment (now session-based, no correlationID - see
 * BrokerController.komFormEnrollment), reads the server-built customAgreementFields back out of
 * /api/offering, then manually replays what the browser/client would do on "Approve" - posting those
 * fields plus the enrollmentRequest data straight to the guest institution's real authentication
 * endpoint - and confirms a real leerovereenkomst PDF comes out the other end, with all eleven fields
 * carried through generiek's EnrollmentRequest/saveCustomAgreementDetails pipeline for real.
 *
 * Only the two genuinely external systems - the guest institution's offering endpoint and the home
 * institution's person endpoint - are mocked (WireMock on 8081); everything between broker and
 * generiek is real HTTP against a real second process.
 *
 * Slower and more fragile than the WireMock-only tests in BrokerControllerTest (spawns a second JVM
 * via `mvn spring-boot:run`), so it's kept isolated in its own class rather than mixed in there.
 */
public class BrokerToGeneriekE2ETest extends AbstractIntegrationTest {

    private static final String GENERIEK_BASE = "http://localhost:8092";

    @RegisterExtension
    WireMockExtension mockServer = new WireMockExtension(8081);

    private Process generiekProcess;

    @BeforeEach
    void startGeneriek() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", "mvn", "-q", "spring-boot:run",
                "-Dspring-boot.run.jvmArguments=-Dconfig.local=true -Dbroker.service_registry_base_url=http://localhost:" + port);
        processBuilder.directory(new File("../../student-mobility-inteken-ontvanger-generiek"));
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(new File("target/generiek-e2e-process.log"));
        generiekProcess = processBuilder.start();
        waitUntilHealthy();
    }

    @AfterEach
    void stopGeneriek() throws InterruptedException {
        if (generiekProcess != null) {
            //`mvn spring-boot:run` forks a child JVM - destroying just the top-level process leaves
            //that child (and generiek's actual listening port) running. Kill the whole tree instead.
            generiekProcess.descendants().forEach(ProcessHandle::destroyForcibly);
            generiekProcess.destroyForcibly();
            generiekProcess.waitFor();
        }
    }

    private void waitUntilHealthy() throws InterruptedException {
        long deadline = System.currentTimeMillis() + 120_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(GENERIEK_BASE + "/internal/health").openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                if (connection.getResponseCode() == 200) {
                    return;
                }
            } catch (IOException ignored) {
                //Not up yet, keep polling
            }
            Thread.sleep(1000);
        }
        throw new IllegalStateException("generiek did not become healthy within 120 seconds - see target/generiek-e2e-process.log");
    }

    @Test
    @SuppressWarnings("unchecked")
    void fullFlowFromBrokerManualEnrolmentToRealPdf() throws IOException {
        stubFor(get(urlPathMatching("/persons/me")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{}")));
        stubFor(get(urlPathMatching("/offerings/1")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(readFile("data/offering-full.json"))));

        //Step 1: real form data into the broker's own endpoint under test - session-based, no
        //correlationID, same as /api/broker
        CookieFilter cookieFilter = new CookieFilter();
        given().redirects().follow(false)
                .filter(cookieFilter)
                .when()
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .param("homeInstitutionSchacHome", "eindhoven.nl")
                .param("guestInstitutionSchacHome", "test-uni.nl")
                .param("offeringId", "1")
                .param("offeringType", "course")
                .post("/api/broker/manual-enrolment")
                .then()
                .statusCode(302)
                .header("Location", "http://localhost:3003?step=approve");

        //Step 2: read back what the client would see on the approve screen - the pre-built
        //customAgreementFields plus the enrollmentRequest/authenticationActionUrl needed to authenticate
        Map<String, Object> offeringResponse = given()
                .filter(cookieFilter)
                .accept(ContentType.JSON)
                .when()
                .get("/api/offering")
                .as(new TypeRef<Map<String, Object>>() {
                });

        Map<String, Object> customAgreementFields = (Map<String, Object>) offeringResponse.get("customAgreementFields");
        Map<String, Object> enrollmentRequest = (Map<String, Object>) offeringResponse.get("enrollmentRequest");
        String authenticationActionUrl = (String) offeringResponse.get("authenticationActionUrl");
        assertNotNull(customAgreementFields);
        assertNotNull(enrollmentRequest);

        //Step 3: replay what the browser's "Approve" click (authentication() in Offering.svelte) would
        //post - personURI/personAuth/associationsURI/homeInstitution/scope/alliance plus the custom
        //agreement fields - straight to the guest institution's real authentication endpoint
        Map<String, Object> formFields = new HashMap<>(enrollmentRequest);
        formFields.putAll(customAgreementFields);

        String authorizeRedirect = given()
                .baseUri(authenticationActionUrl)
                .redirects().follow(false)
                .when()
                .header("Content-Type", APPLICATION_FORM_URLENCODED_VALUE)
                .params(formFields)
                .post()
                .then()
                .statusCode(302)
                .extract()
                .header("Location");

        //Step 4: follow the (config.local-bypassed) self-redirect to /redirect_uri on the real process
        String finalRedirect = given()
                .redirects().follow(false)
                .when()
                .get(authorizeRedirect)
                .then()
                .statusCode(302)
                .extract()
                .header("Location");

        MultiValueMap<String, String> redirectParams = UriComponentsBuilder.fromHttpUrl(finalRedirect).build().getQueryParams();
        String correlationID = redirectParams.getFirst("correlationID");
        assertNotNull(correlationID);

        //Step 5: fetch and verify the real PDF generated by the real generiek process, with all eleven
        //custom agreement fields now carried all the way through
        byte[] pdf = given()
                .baseUri(GENERIEK_BASE)
                .when()
                .queryParam("correlationID", correlationID)
                .get("/api/leerovereenkomst")
                .then()
                .statusCode(200)
                .contentType("application/pdf")
                .extract()
                .asByteArray();

        assertTrue(pdf.length > 0);
        Files.write(Paths.get("target/leerovereenkomst-real-e2e.pdf"), pdf);

        try (PDDocument document = PDDocument.load(pdf)) {
            String text = new PDFTextStripper().getText(document);
            assertTrue(text.contains("Quantum Computing Advanced"));
            assertTrue(text.contains("QCF-2026"));
            assertTrue(text.contains("Creative Business"));
            assertTrue(text.contains("full-time"));
            assertTrue(text.contains("Completed first-year mathematics"));
            assertTrue(text.contains("Presentations and written exam"));
            assertTrue(text.contains("Eindhoven University of Technology"));
            assertTrue(text.contains("Test University"));
        }
    }
}
