package broker.api;

import broker.AbstractIntegrationTest;
import broker.domain.PollRequest;
import broker.domain.PollResult;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.icegreen.greenmail.util.GreenMailUtil.getBody;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;

public class PollControllerTest extends AbstractIntegrationTest {

    @RegisterExtension
    public static final GreenMailExtension greenMail = new GreenMailExtension(
            new ServerSetup(1025, null, ServerSetup.PROTOCOL_SMTP));

    @Test
    void pollRequest() throws MessagingException {
        PollRequest pollRequest = new PollRequest(PollResult.veryEasy, "Just because", "John Doe");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(pollRequest)
                .post("/api/poll")
                .then()
                .statusCode(HttpStatus.OK.value());

        MimeMessage mimeMessage = mailMessage();
        assertEquals("support@surf.nl", mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());

        String body = getBody(mimeMessage);

        assertTrue(body.contains(pollRequest.getPoll().getDisplayName()));
        assertTrue(body.contains(pollRequest.getMotivation()));

    }

    private MimeMessage mailMessage() {
        await().until(() -> greenMail.getReceivedMessages().length != 0);
        return greenMail.getReceivedMessages()[0];
    }


}