package broker.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

@Configuration
public class MailConf {

    @Bean
    public MailBox mailBox(@Value("${email.from}") String emailFrom,
                           @Value("${config.poll_email}") String pollEmail,
                           @Value("${email.environment}") String env,
                           JavaMailSender mailSender) throws IOException {
        return new MailBox(mailSender, emailFrom, pollEmail, env);
    }


}
