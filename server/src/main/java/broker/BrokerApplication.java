package broker;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrokerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BrokerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
