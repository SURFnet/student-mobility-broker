package broker.queue;

import broker.domain.Institution;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueueServiceTest {

    private final QueueService queueService = new QueueService(
            "http://localhost:8082",
            "edubrokersurf",
            "http://localhost:8083/start");
    private final Institution institution = new Institution(true, "queue1", "secret");

    @Test
    void validateQueueToken() {
        String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                institution.getQueueItWaitingRoom(),
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 - 15_000_000);
        String withoutHash = queueService.generateSHA256Hash(institution.getQueueItSecret(), token);
        final String queueItToken = token + "~h_" + withoutHash;
        //No exception is thrown
        queueService.validateQueueToken(institution, queueItToken);
    }

    @Test
    void validateQueueTokenReplay() {
        final String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                institution.getQueueItWaitingRoom(),
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 + 15_000_000);
        assertThrows(IllegalArgumentException.class, () -> queueService.validateQueueToken(institution, token));
    }

    @Test
    void validateQueueTokenNoHash() {
        final String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                institution.getQueueItWaitingRoom(),
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 - 15_000_000);
        assertThrows(IllegalArgumentException.class, () -> queueService.validateQueueToken(institution, token));
    }

    @Test
    void validateQueueTokenException() {
        String token = "ce_true~rt_queue~bogus";
        String withoutHash = queueService.generateSHA256Hash(institution.getQueueItSecret(), token);
        final String queueItToken = token + "~h_" + withoutHash + "X";

        assertThrows(IllegalArgumentException.class, () -> queueService.validateQueueToken(institution, queueItToken));
    }

    @Test
    void getRedirectUrl() {
        assertEquals("http://localhost:8082?c=edubrokersurf&e=queue1&t=http%3A%2F%2Flocalhost%3A8083%2Fstart",
                queueService.getRedirectUrl(institution));
    }
}