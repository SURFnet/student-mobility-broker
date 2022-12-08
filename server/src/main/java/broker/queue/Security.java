package broker.queue;

import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class Security {

    private Security() {
    }

    private static final String algorithm = "HmacSHA256";
    private static final String charSet = Charset.defaultCharset().name();

    @SneakyThrows
    public static String generateSHA256Hash(String secretKey, String stringToHash) {
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
