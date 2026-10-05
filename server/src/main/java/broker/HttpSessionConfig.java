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
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "database-session-enabled", havingValue = "true", matchIfMissing = false)
@Import(SessionAutoConfiguration.class)
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 8)
public class HttpSessionConfig {

    @Bean(destroyMethod = "close")
    HikariDataSource dataSource(@Value("${datasource.driver-class-name}") String driver,
                                @Value("${datasource.url}") String url,
                                @Value("${datasource.username}") String username,
                                @Value("${datasource.password}") String password,
                                @Value("${datasource.pool.maximum-pool-size:10}") int maximumPoolSize,
                                @Value("${datasource.pool.minimum-idle:2}") int minimumIdle) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("session-pool");
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);

        return dataSource;
    }

    @Bean
    JdbcSessionDataSourceScriptDatabaseInitializer scriptInitializer(DataSource dataSource) {
        JdbcSessionProperties properties = new JdbcSessionProperties();
        properties.setInitializeSchema(DatabaseInitializationMode.ALWAYS);
        return new JdbcSessionDataSourceScriptDatabaseInitializer(dataSource, properties);
    }

}
