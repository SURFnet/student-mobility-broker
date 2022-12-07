package broker.queue;

import broker.domain.Institution;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueueService {

    private static final Log LOG = LogFactory.getLog(QueueService.class);

    private static final String algorithm = "HmacSHA256";
    private static final String charSet = Charset.defaultCharset().name();
    private static final String withoutHash = "W";

    private final String customerId;
    private final String url;
    private final String redirectUri;

    public QueueService(
            @Value("${queueit.url}") String url,
            @Value("${queueit.customer_id}") String customerId,
            @Value("${queueit.redirect_uri}") String redirectUri) {
        this.url = url;
        this.customerId = customerId;
        this.redirectUri = redirectUri;
    }

    @SneakyThrows
    public String getRedirectUrl(Institution institution) {
        String redirectUriWithInstitution = redirectUri + "?i=" + institution.getSchacHome();
        return String.format("%s?c=%s&e=%s&t=%s",
                this.url,
                this.customerId,
                institution.getQueueItWaitingRoom(),
                redirectUriWithInstitution);
    }

    public boolean validateQueueToken(Institution institution, String queueItToken) {
        Map<String, String> queueParams = this.parse(queueItToken);
        //Validate timestamp, hash, queue
        if (queueParams.containsKey("ts")) {
            if (Long.parseLong(queueParams.get("ts")) < (System.currentTimeMillis() / 1000L)) {
                LOG.warn("Invalid timestamp, got " + queueParams.get("ts"));
                return false;
            }
        }
        if (queueParams.containsKey("e")) {
            if (!queueParams.get("e").equalsIgnoreCase(institution.getQueueItWaitingRoom())) {
                LOG.warn(String.format("Queue mismatch, expected %s, but got %s",
                        institution.getQueueItWaitingRoom(),
                        queueParams.get("e")));
                return false;
            }
        }
        if (!queueParams.containsKey("h")) {
            LOG.warn("Queue token does not contain hash, " + queueParams);
        }
        String calculatedHash = generateSHA256Hash(institution.getQueueItSecret(), queueParams.get(withoutHash));
        if (!calculatedHash.equalsIgnoreCase(queueParams.get("h"))) {
            LOG.warn(String.format("Hash value is different, expected %, got %s", calculatedHash, queueParams.get("h")));
            return false;
        }
        return true;
    }

    private Map<String, String> parse(String queueItToken) {
        List<String> parts = Arrays.asList(queueItToken.split("~"));
        Map<String, String> results = parts.stream()
                .map(s -> Arrays.asList(s.split("_")))
                .filter(l -> l.size() == 2)
                .collect(Collectors.toMap(l -> l.get(0), l -> l.get(1)));
        results.put(withoutHash, queueItToken.replace("~h_" + results.get("h"), ""));
        return results;
    }

    @SneakyThrows
    protected String generateSHA256Hash(String secretKey, String stringToHash) {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(charSet), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(stringToHash.getBytes(charSet));
        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(String.format("%1$02x", b));
        }
        return sb.toString();
    }
}
