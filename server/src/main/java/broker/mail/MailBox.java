package broker.mail;

import broker.domain.PollRequest;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MailBox {

    private final JavaMailSender mailSender;
    private final String emailFrom;
    private final String contactEmail;
    private final String environment;

    private final MustacheFactory mustacheFactory = new DefaultMustacheFactory("templates");

    public MailBox(JavaMailSender mailSender,
                   String emailFrom,
                   String contactEmail,
                   String environment)  {
        this.mailSender = mailSender;
        this.emailFrom = emailFrom;
        this.contactEmail = contactEmail;
        this.environment = environment;
    }

    @SneakyThrows
    public void sendPollMail(PollRequest pollRequest) {
        Map<String, Object> variables = new HashMap<>();
        if (!environment.equalsIgnoreCase("prod")) {
            variables.put("environment", environment);
        }
        variables.put("pollRequest", pollRequest);
        sendMail("poll_en", "Poll result", variables, contactEmail);
    }

    @SneakyThrows
    private String sendMail(String templateName, String subject, Map<String, Object> variables, String... to) {
        String htmlText = this.mailTemplate(templateName + ".html", variables);
        String plainText = this.mailTemplate(templateName + ".txt", variables);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject);
        helper.setText(plainText, htmlText);
        helper.setTo(to);
        helper.setFrom(emailFrom);
        new Thread(() -> mailSender.send(mimeMessage)).start();
        return htmlText;
    }

    private String mailTemplate(String templateName, Map<String, Object> context) {
        return mustacheFactory.compile(templateName).execute(new StringWriter(), context).toString();
    }

}
