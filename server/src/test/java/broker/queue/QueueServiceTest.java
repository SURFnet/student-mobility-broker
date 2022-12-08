package broker.queue;

import broker.domain.Institution;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
                System.currentTimeMillis() / 1000 + 15_000_000);
        String withoutHash = queueService.generateSHA256Hash(institution.getQueueItSecret(), token);
        String queueItToken = token + "~h_" + withoutHash;
        assertTrue(queueService.validateQueueToken(institution, queueItToken));
    }

    @Test
    void validateQueueTokenReplay() {
        String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                institution.getQueueItWaitingRoom(),
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 - 15_000_000);
        assertFalse(queueService.validateQueueToken(institution, token));
    }

    @Test
    void validateQueueTokenNoHash() {
        String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                institution.getQueueItWaitingRoom(),
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 + 15_000_000);
        assertFalse(queueService.validateQueueToken(institution, token));
    }

    @Test
    void validateQueueTokenException() {
        String token = "ce_true~rt_queue~bogus";
        String withoutHash = queueService.generateSHA256Hash(institution.getQueueItSecret(), token);
        String queueItToken = token + "~h_" + withoutHash + "X";
        assertFalse(queueService.validateQueueToken(institution, queueItToken));
    }

    @Test
    void validateQueueTokenWrongQueue() {
        String token = String.format("e_%s~q_%s~ts_%s~ce_true~rt_queue",
                "nope",
                UUID.randomUUID(),
                System.currentTimeMillis() / 1000 + 15_000_000);
        String withoutHash = queueService.generateSHA256Hash(institution.getQueueItSecret(), token);
        String queueItToken = token + "~h_" + withoutHash;
        assertFalse(queueService.validateQueueToken(institution, queueItToken));
    }

    @Test
    void getRedirectUrl() {
        assertEquals("http://localhost:8082?c=edubrokersurf&e=queue1&t=http://localhost:8083/start",
                queueService.getRedirectUrl(institution));
    }
}