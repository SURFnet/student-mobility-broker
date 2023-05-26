package broker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.session.JdbcSessionDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.session.JdbcSessionProperties;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "database-session-enabled", havingValue = "true", matchIfMissing = false)
@Import(SessionAutoConfiguration.class)
@EnableJdbcHttpSession
public class HttpSessionConfig {

    @Bean
    DataSource dataSource(@Value("${datasource.driver-class-name}") String driver,
                          @Value("${datasource.url}") String url,
                          @Value("${datasource.username}") String username,
                          @Value("${datasource.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    JdbcSessionDataSourceScriptDatabaseInitializer scriptInitializer(DataSource dataSource) {
        JdbcSessionProperties properties = new JdbcSessionProperties();
        properties.setInitializeSchema(DatabaseInitializationMode.ALWAYS);
        return new JdbcSessionDataSourceScriptDatabaseInitializer(dataSource, properties);
    }

}
