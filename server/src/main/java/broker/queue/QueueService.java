package broker.queue;

import broker.domain.Institution;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        return String.format("%s?c=%s&e=%s&t=%s",
                this.url,
                this.customerId,
                institution.getQueueItWaitingRoom(),
                URLEncoder.encode(redirectUri, charSet));
    }

    public void validateQueueToken(Institution institution, String queueItToken) {
        Map<String, String> queueParams = this.parse(queueItToken);
        //Validate timestamp, hash, queue
        if (queueParams.containsKey("ts")) {
            Assert.isTrue(Long.parseLong(queueParams.get("ts")) < System.currentTimeMillis() / 1000L,
                    "Invalid timestamp");
        }
        if (queueParams.containsKey("e")) {
            Assert.isTrue(queueParams.get("e").equalsIgnoreCase(institution.getQueueItWaitingRoom()),
                    String.format("Queue mismatch, expected %s, but got %s",
                            institution.getQueueItWaitingRoom(),
                            queueParams.get("e")));
        }
        if (!queueParams.containsKey("h")) {
            throw new IllegalArgumentException("Queue token does not contain hash");
        }
        String calculatedHash = generateSHA256Hash(institution.getQueueItSecret(), queueParams.get(withoutHash));
        Assert.isTrue(calculatedHash.equalsIgnoreCase(queueParams.get("h")), "Hash value is different");
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
